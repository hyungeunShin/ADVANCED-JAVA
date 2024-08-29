package thread.executor.future;

import util.ThreadUtils;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class FutureExceptionMain {
    /*
    - 요청 스레드: es.submit(new ExCallable()) 을 호출해서 작업을 전달한다.
    - 작업 스레드: ExCallable 을 실행하는데 IllegalStateException 예외가 발생한다.
        - 작업 스레드는 Future 에 발생한 예외를 담아둔다. 참고로 예외도 객체이다. 잡아서 필드에 보관할 수 있다.
        - 예외가 발생했으므로 Future 의 상태는 FAILED 가 된다.
    - 요청 스레드: 결과를 얻기 위해 future.get() 을 호출한다.
        - Future 의 상태가 FAILED 면 ExecutionException 예외를 던진다.
        - 이 예외는 내부에 앞서 Future 에 저장해둔 IllegalStateException 을 포함하고 있다.
        - e.getCause() 을 호출하면 작업에서 발생한 원본 예외를 받을 수 있다.

    Future.get() 은 작업의 결과 값을 받을 수도 있고 예외를 받을 수도 있다.
    */

    public static void main(String[] args) {
        try(ExecutorService es = Executors.newFixedThreadPool(1)) {
            log("작업 전달");
            Future<Integer> future = es.submit(new ExCallable());

            ThreadUtils.sleep(1000);

            log("future.get() 호출 시도, future.state() : " + future.state());
            Integer result = future.get();
            log("result : " + result);
        } catch(ExecutionException e) {
            log("e : " + e);
            Throwable cause = e.getCause();
            log("cause : " + cause);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class ExCallable implements Callable<Integer> {
        @Override
        public Integer call() {
            log("Callable 실행, 예외 발생");
            throw new IllegalStateException("ex");
        }
    }
}
