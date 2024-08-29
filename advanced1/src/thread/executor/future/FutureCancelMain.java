package thread.executor.future;

import util.ThreadUtils;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class FutureCancelMain {
    /*
    - cancel(true) : Future 를 취소 상태로 변경한다. 이때 작업이 실행중이라면 Thread.interrupt() 를 호출해서 작업을 중단한다.
        mayInterruptIfRunning=true 를 사용하면 실행중인 작업에 인터럽트가 발생해서 실행중인 작업을 중지시키려 시도한다.
        이후 Future.get() 을 호출하면 CancellationException 런타임 예외가 발생한다.

    - cancel(false) : Future 를 취소 상태로 변경한다. 단 이미 실행 중인 작업을 중단하지는 않는다.
        mayInterruptIfRunning=false 를 사용하면 실행중인 작업은 그냥 둔다. (인터럽트를 걸지 않는다.)
        실행중인 작업은 그냥 두더라도 cancel() 을 호출했기 때문에 Future 는 CANCEL 상태가 된다.
        이후 Future.get() 을 호출하면 CancellationException 런타임 예외가 발생한다.
    */

    private static final boolean mayInterruptIfRunning = true;
    //private static final boolean mayInterruptIfRunning = false;

    public static void main(String[] args) {
        try(ExecutorService es = Executors.newFixedThreadPool(1)) {
            Future<String> future = es.submit(new MyTask());
            log("Future.state : " + future.state());

            ThreadUtils.sleep(3000);

            //취소
            log("future.cancel(" + mayInterruptIfRunning + ") 호출");
            boolean result = future.cancel(mayInterruptIfRunning);
            log("Future.state : " + future.state());
            log("cancel(" + mayInterruptIfRunning + ") result: " + result);

            log("Future result : " + future.get());
        } catch(CancellationException e) {  //런타임 예외
            log("Future 는 이미 취소되었습니다.");
        } catch(ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyTask implements Callable<String> {
        @Override
        public String call() {
            try {
                for(int i = 0; i < 10; i++) {
                    log("작업 중 : " + i);
                    Thread.sleep(1000);
                }
            } catch(InterruptedException e) {
                log("인터럽트 발생");
                return "Interrupted";
            }

            return "Completed";
        }
    }
}
