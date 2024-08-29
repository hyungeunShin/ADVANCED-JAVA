package thread.cas.increment;

import util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

public class IncrementThreadMain {
    public static final int THREAD_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        test(new IncrementIntegerV1());
        //volatile 은 여러 스레드 사이에 발생하는 캐시 메모리와 메인 메모리가 동기화 되지 않는 문제를 해결할 뿐이다.
        test(new IncrementIntegerV2());
        //연산 자체가 나누어진 경우에는 synchronized 블럭이나 Lock 등을 사용해서 안전한 임계 영역을 만들어야 한다.
        test(new IncrementIntegerV3());
        //자바가 제공하는 AtomicInteger 를 사용한다. 멀티스레드 상황에 안전하게 사용할 수 있다.
        test(new IncrementIntegerV4());
    }

    private static void test(IncrementInteger incrementInteger) throws InterruptedException {
        Runnable runnable = () -> {
            ThreadUtils.sleep(10);
            incrementInteger.increment();
        };

        List<Thread> list = new ArrayList<>();
        for(int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(runnable);
            list.add(thread);
            thread.start();
        }

        for(Thread thread : list) {
            thread.join();
        }

        int result = incrementInteger.get();
        System.out.println(incrementInteger.getClass().getSimpleName() + ".value : " + result);
    }
}
