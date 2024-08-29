package thread.executor.future;

import util.ThreadUtils;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;

public class CallableMainV2 {
    /*
    MyCallable 인스턴스를 편의상 taskA 라고 하겠다.
    편의상 스레드풀에 스레드가 1개 있다고 가정하겠다.

    [로그] submit() 호출
        - submit() 을 호출해서 ExecutorService 에 taskA 를 전달한다.
        - 요청 스레드는 es.submit(taskA) 를 호출하고 있는 중이다.
        - ExecutorService 는 전달한 taskA 의 미래 결과를 알 수 있는 Future 객체를 생성한다.
            - Future 는 인터페이스이다. 이때 생성되는 실제 구현체는 FutureTask 이다.
        - 그리고 생성한 Future 객체 안에 taskA 의 인스턴스를 보관한다.
        - Future 는 내부에 taskA 작업의 완료 여부와 작업의 결과 값을 가진다.
        - submit() 을 호출한 경우 Future 가 만들어지고 전달한 작업인 taskA 가 바로 블로킹 큐에 담기는 것이 아니라 taskA 를 감싸고 있는 Future 가 대신 블로킹 큐에 담긴다.

    [로그] Callable 시작
        - 큐에 들어있는 Future[taskA] 를 꺼내서 스레드 풀의 스레드1이 작업을 시작한다.
        - 참고로 Future 의 구현체인 FutureTask 는 Runnable 인터페이스도 함께 구현하고 있다.
        - 스레드1은 FutureTask 의 run() 메서드를 수행한다.
        - 그리고 run() 메서드가 taskA 의 call() 메서드를 호출하고 그 결과를 받아서 처리한다.
            - FutureTask.run() -> MyCallable.call()

    [로그] future 즉시 반환 : java.util.concurrent.FutureTask@c4437c4[Not completed, task = thread.executor.future.CallableMainV2$MyCallable@2e817b38]
        - Future 는 내부에 작업의 완료 여부와 작업의 결과 값을 가진다. 작업이 완료되지 않았기 때문에 아직은 결과 값이 없다.
            - 로그를 보면 Future 의 구현체는 FutureTask 이다.
            - Future 의 상태는 "Not completed"(미 완료)이고 연관된 작업은 전달한 taskA(MyCallable 인스턴스) 이다.
        - 여기서 중요한 핵심이 있는데 작업을 전달할 때 생성된 Future 는 즉시 반환된다는 점이다.

    [로그] future.get() [블로킹] 메소드 호출 시작 -> 메인 스레드 WAITING
        - 생성한 Future 를 즉시 반환하기 때문에 요청 스레드는 대기하지 않고 자유롭게 본인의 다음 코드를 호출할 수 있다.
            - 이것은 마치 Thread.start() 를 호출한 것과 비슷하다. Thread.start() 를 호출하면 스레드의 작업 코드가 별도의 스레드에서 실행된다.
              요청 스레드는 대기하지 않고 즉시 다음 코드를 호출할 수 있다.

        스레드1
            - 스레드1은 taskA 의 작업을 아직 처리중이다. 아직 완료하지는 않았다.
        요청 스레드
            - 요청 스레드는 Future 인스턴스의 참조를 가지고 있다.
            - 그리고 언제든지 본인이 필요할 때 Future.get() 을 호출해서 taskA 작업의 미래 결과를 받을 수 있다.
            - 요청 스레드는 작업의 결과가 필요해서 future.get() 을 호출한다.
                - Future 에는 완료 상태가 있다. taskA 의 작업이 완료되면 Future 의 상태도 완료로 변경된다.
                - 그런데 여기서 taskA 의 작업이 아직 완료되지 않았다. 따라서 Future 도 완료 상태가 아니다.
            - 요청 스레드가 future.get() 을 호출하면 Future 가 완료 상태가 될 때 까지 대기한다. 이때 요청 스레드의 상태는 RUNNABLE -> WAITING 이 된다.

        future.get() 을 호출했을 때
            - Future 가 완료 상태: Future 가 완료 상태면 Future 에 결과도 포함되어 있다. 이 경우 요청 스레드는 대기하지 않고 값을 즉시 반환받을 수 있다.
            - Future 가 완료 상태가 아님: taskA 가 아직 수행되지 않았거나 또는 수행 중이라는 뜻이다. 이때는 어쩔 수 없이 요청 스레드가 결과를 받기 위해 대기해야 한다.
              요청 스레드가 마치 락을 얻을 때처럼, 결과를 얻기 위해 대기한다.
              이처럼 스레드가 어떤 결과를 얻기 위해 대기하는 것을 블로킹(Blocking)이라 한다.

    [로그] 메인 스레드 상태 : WAITING
    [로그] create value : 1
    [로그] Callable 완료
        요청 스레드
            - 대기(WAITING) 상태로 future.get() 을 호출하고 대기중이다.
        스레드1
            1. taskA 작업을 완료한다.
            2. Future 에 taskA 의 반환 결과를 담는다.
            3. Future 의 상태를 완료로 변경한다.
            4. 요청 스레드를 깨운다. 요청 스레드는 WAITING -> RUNNABLE 상태로 변한다.

    [로그] future.get() [블로킹] 메서드 호출 완료 -> 메인 스레드 RUNNABLE
    [로그] result value : 1
        요청 스레드
            - 요청 스레드는 RUNNABLE 상태가 되었다. 그리고 완료 상태의 Future 에서 결과를 반환 받는다. 참고로 taskA 의 결과가 Future 에 담겨있다.
        스레드1
            - 작업을 마친 스레드1은 스레드 풀로 반환된다. RUNNABLE -> WAITING

    [로그] future 완료 : java.util.concurrent.FutureTask@c4437c4[Completed normally]
        - Future 의 인스턴스인 FutureTask 를 보면 "Completed normally"로 정상 완료된 것을 확인할 수 있다.
    */
    public static void main(String[] args) {
        try(ExecutorService es = Executors.newFixedThreadPool(1)) {
            log("submit() 호출");
            //future 는 즉시 반환된다. 덕분에 요청 스레드는 블로킹 되지 않고 필요한 작업을 할 수 있다.
            Future<Integer> future = es.submit(new MyCallable(Thread.currentThread()));
            log("future 즉시 반환 : " + future);

            log("future.get() [블로킹] 메소드 호출 시작 -> 메인 스레드 WAITING");
            /*
            Future 가 완료 상태: Future 가 완료 상태면 Future 에 결과도 포함되어 있다. 이 경우 요청 스레드는 대기하지 않고 값을 즉시 반환받을 수 있다.
            Future 가 완료 상태가 아님: 작업이 아직 수행되지 않았거나 또는 수행 중이라는 뜻이다. 이때는 어쩔 수 없이 요청 스레드가 결과를 받기 위해 블로킹 상태로 대기해야 한다.
            */
            Integer result = future.get();
            log("future.get() [블로킹] 메서드 호출 완료 -> 메인 스레드 RUNNABLE");

            log("result value : " + result);
            log("future 완료 : " + future);
        } catch(ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyCallable implements Callable<Integer> {
        private final Thread requestThread;

        public MyCallable(Thread requestThread) {
            this.requestThread = requestThread;
        }

        @Override
        public Integer call() {
            log("Callable 시작");
            ThreadUtils.sleep(2000);
            log("메인 스레드 상태 : " + requestThread.getState());
            int value = new Random().nextInt(10);
            log("create value : " + value);
            log("Callable 완료");
            return value;
        }
    }
}
