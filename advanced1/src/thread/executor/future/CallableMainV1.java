package thread.executor.future;

import util.ThreadUtils;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;

public class CallableMainV1 {
    /*
    Executor 프레임워크의 강점
        요청 스레드가 결과를 받아야 하는 상황이라면 Callable 을 사용한 방식은 Runnable 을 사용하는 방식보다 훨씬 편리하다.
        코드만 보면 복잡한 멀티스레드를 사용한다는 느낌보다는 단순한 싱글 스레드 방식으로 개발한다는 느낌이 들 것이다.
        이 과정에서 내가 스레드를 생성하거나 join() 으로 스레드를 제어하거나 한 코드는 전혀 없다.
        심지어 Thread 라는 코드도 없다.
        단순하게 ExecutorService 에 필요한 작업을 요청하고 결과를 받아서 쓰면 된다.
        복잡한 멀티스레드를 매우 편리하게 사용할 수 있는 것이 바로 Executor 프레임워크의 큰 강점이다.

    여기서 잘 생각해보면 한 가지 애매한 상황이 있다.
    future.get() 을 호출하는 요청 스레드(main)는 future.get() 을 호출 했을 때 2가지 상황으로 나뉘게 된다.
        - MyCallable 작업을 처리하는 스레드 풀의 스레드가 작업을 완료했다.
        - MyCallable 작업을 처리하는 스레드 풀의 스레드가 아직 작업을 완료하지 못했다.
    future.get() 을 호출했을 때 스레드 풀의 스레드가 작업을 완료했다면 반환 받을 결과가 있을 것이다.
    그런데 아직 작업을 처리중이라면 어떻게 될까?
    왜 결과를 바로 반환하지 않고 불편하게 Future 라는 객체를 대신 반환할까?
    */

    public static void main(String[] args) {
        //new ThreadPoolExecutor(1,1,0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()); 와 동일
        try(ExecutorService es = Executors.newFixedThreadPool(1)) {
            //Future<Integer> future = es.submit(new MyCallable());
            //Integer result = future.get();

            Integer result = es.submit(new MyCallable()).get();

            log("result value : " + result);
        } catch(ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() {
            log("Callable 시작");
            ThreadUtils.sleep(2000);
            int value = new Random().nextInt(10);
            log("create value : " + value);
            log("Callable 완료");
            return value;
        }
    }
}
