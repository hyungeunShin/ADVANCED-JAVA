package thread.executor;

import thread.executor.util.ExecutorUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;

public class ExecutorShutdownMain {
    /*
    shutdown() 을 호출해서 이미 들어온 모든 작업을 다 처리하고 서비스를 우아하게 종료(graceful shutdown)하는 것이 가장 이상적이다.
    하지만 갑자기 요청이 너무 많이 들어와서 큐에 대기중인 작업이 너무 많아 작업 완료가 어렵거나 작업이 너무 오래 걸리거나 또는 버그가 발생해서 특정 작업이 끝나지 않을 수 있다.
    이렇게 되면 서비스가 너무 늦게 종료되거나 종료되지 않는 문제가 발생할 수 있다.
    이럴 때는 보통 우아하게 종료하는 시간을 정한다. 예를 들어서 60초까지는 작업을 다 처리할 수 있게 기다리는 것이다.
    그리고 60초가 지나면 무언가 문제가 있다고 가정하고 shutdownNow() 를 호출해서 작업들을 강제로 종료한다.

    close() 의 경우 이렇게 구현되어 있다.
    shutdown() 을 호출하고 하루를 기다려도 작업이 완료되지 않으면 shutdownNow() 를 호출한다.
    그런데 대부분 하루를 기다릴 수는 없을 것이다.

    방금 설명한데로 우선은 shutdown() 을 통해 우아한 종료를 시도하고 10초간 종료되지 않으면 shutdownNow()를 통해 강제 종료하는 방식을 구현하자
    참고로 구현할 shutdownAndAwaitTermination() 은 ExecutorService 공식 API 문서에서 제안하는 방식이다.
    */

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(2);
        es.execute(new RunnableTask("taskA"));
        es.execute(new RunnableTask("taskB"));
        es.execute(new RunnableTask("taskC"));
        es.execute(new RunnableTask("logTask", 100_000));   //100초 대기

        ExecutorUtils.printState(es);

        log("== shutdown 시작 ==");
        shutdownAndAwaitTermination(es);
        log("== shutdown 완료 ==");
        ExecutorUtils.printState(es);
    }

    /*
    es.shutdown();
        - 새로운 작업을 받지 않는다. 처리 중이거나 큐에 이미 대기중인 작업은 처리한다. 이후에 풀의 스레드를 종료한다.
        - shutdown() 은 블로킹 메서드가 아니다. 서비스가 종료될 때 까지 main 스레드가 대기하지 않는다. main 스레드는 바로 다음 코드를 호출한다.

    if(!es.awaitTermination(10, TimeUnit.SECONDS)) {...}
        - 블로킹 메서드이다.
        - main 스레드는 대기하며 서비스 종료를 10초간 기다린다.
            - 만약 10초 안에 모든 작업이 완료된다면 true 를 반환한다.
        - 여기서 taskA, taskB, taskC 의 수행이 완료된다. 그런데 longTask 는 10초가 지나도 완료되지 않았다.
            - 따라서 false 를 반환한다.

    log("서비스 정상 종료 실패 -> 강제 종료 시도");
    es.shutdownNow();
    if(!es.awaitTermination(10, TimeUnit.SECONDS)) {...}
        - 정상 종료가 10초 이상 걸렸다.
        - shutdownNow() 를 통해 강제 종료에 들어간다. shutdown() 과 마찬가지로 블로킹 메서드가 아니다.
        - 강제 종료를 하면 작업 중인 스레드에 인터럽트가 발생한다. 다음 로그를 통해 인터럽트를 확인할 수 있다.
        - 인터럽트가 발생하면서 스레드도 작업을 종료하고 shutdownNow() 를 통한 강제 shutdown 도 완료된다.

    그런데 마지막에 강제 종료인 es.shutdownNow() 를 호출한 다음에 왜 10초간 또 기다릴까?
    shutdownNow() 가 작업 중인 스레드에 인터럽트를 호출하는 것은 맞다.
    인터럽트를 호출하더라도 여러가지 이유로 작업에 시간이 걸릴 수 있다.
    인터럽트 이후에 자원을 정리하는 어떤 간단한 작업을 수행할 수 도 있다. 이런 시간을 기다려주는 것이다.
    */
    static void shutdownAndAwaitTermination(ExecutorService es) {
        es.shutdown();  //non-blocking, 새로운 작업을 받지 않는다. 처리 중이거나 큐에 이미 대기중인 작업은 처리한다. 이후에 풀의 스레드를 종료한다.

        try {
            //이미 대기중인 작업들을 모두 완료할 때 까지 10초 기다린다.
            log("서비스 정상 종료 시도");
            if(!es.awaitTermination(10, TimeUnit.SECONDS)) {
                //정상 종료가 너무 오래 걸리면
                log("서비스 정상 종료 실패 -> 강제 종료 시도");
                es.shutdownNow();

                //작업이 취소될 때 까지 대기한다.
                if(!es.awaitTermination(10, TimeUnit.SECONDS)) {
                    log("서비스가 종료되지 않았습니다.");
                }
            }
        } catch(InterruptedException e) {
            //awaitTermination()으로 대기중인 현재 스레드가 인터럽트 될 수 있다.
            es.shutdownNow();
        }
    }
}
