package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.util.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ExecutorBasicMain {
    public static void main(String[] args) {
        /*
        ExecutorService 의 가장 대표적인 구현체는 ThreadPoolExecutor 이다.

        ThreadPoolExecutor(ExecutorService) 는 크게 2가지 요소로 구성되어 있다.
            - 스레드 풀: 스레드를 관리한다.
            - BlockingQueue : 작업을 보관한다. 생산자 소비자 문제를 해결하기 위해 단순한 큐가 아니라 BlockingQueue 를 사용한다.

        ThreadPoolExecutor 의 생성자는 다음 속성을 사용한다.
            - corePoolSize : 스레드 풀에서 관리되는 기본 스레드의 수
            - maximumPoolSize : 스레드 풀에서 관리되는 최대 스레드 수
            - keepAliveTime, TimeUnit unit : 기본 스레드 수를 초과해서 만들어진 스레드가 생존할 수 있는 대기 시간이다. 이 시간 동안 처리할 작업이 없다면 초과 스레드는 제거된다.
            - BlockingQueue workQueue : 작업을 보관할 블로킹 큐
        */
        try(ExecutorService es = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>())) {
            log("== 초기 상태 ==");
            printState(es);

            /*
            생산자가 es.execute(new RunnableTask("taskA")) 를 호출하면 RunnableTask("taskA") 인스턴스가 BlockingQueue 에 보관된다.
            생산자: es.execute(작업) 를 호출하면 내부에서 BlockingQueue 에 작업을 보관한다. main 스레드가 생산자가 된다.
            소비자: 스레드 풀에 있는 스레드가 소비자이다. 이후에 소비자 중에 하나가 BlockingQueue 에 들어있는 작업을 받아서 처리한다.

            - main 스레드가 es.execute("taskA ~ taskD") 를 호출한다.
                - 참고로 당연한 이야기지만 main 스레드는 작업을 전달하고 기다리지 않는다. 전달한 작업은 다른 스레드가 실행할 것이다.
                  main 스레드는 작업을 큐에 보관까지만 하고 바로 다음 코드를 수행한다.
            - taskA~D 요청이 블로킹 큐에 들어온다.
            - 최초의 작업이 들어오면 이때 작업을 처리하기 위해 스레드를 만든다.
                - 참고로 스레드 풀에 스레드를 미리 만들어두지는 않는다.
            - 작업이 들어올 때 마다 corePoolSize 의 크기까지 스레드를 만든다.
                - 예를 들어서 최초 작업인 taskA 가 들어오는 시점에 스레드1을 생성하고 다음 작업인 taskB 가 들어오는 시점에 스레드2를 생성한다.
                - 이런 방식으로 corePoolSize 에 지정한 수 만큼 스레드를 스레드 풀에 만든다. 여기서는 2를 설정했으므로 2개까지 만든다.
                - corePoolSize 까지 스레드가 생성되고 나면 이후에는 스레드를 생성하지 않고 앞서 만든 스레드를 재사용한다.
            */
            es.execute(new RunnableTask("taskA"));
            es.execute(new RunnableTask("taskB"));
            es.execute(new RunnableTask("taskC"));
            es.execute(new RunnableTask("taskD"));

            log("== 작업 수행 중 ==");
            printState(es);
            sleep(3000);

            log("== 작업 수행 완료 ==");
            printState(es);

            log("== shutdown 완료 ==");
            printState(es);
        }
    }
}
