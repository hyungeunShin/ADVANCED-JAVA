package thread.cas;

import util.MyLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV3 {
    private static final int THREAD_COUNT = 5;

    /*
    CAS 는 락을 사용하지 않고 대신에 다른 스레드가 값을 먼저 증가해서 문제가 발생하는 경우 루프를 돌며 재시도를 하는 방식을 사용한다.

    두 스레드가 동시에 실행되면서 문제가 발생하는 상황을 스레드가 충돌했다고 표현한다.
    이 과정에서 충돌이 발생할 때마다 반복해서 다시 시도하므로 결과적으로 락 없이 데이터를 안전하게 변경할 수 있다.
    CAS 를 사용하는 방식은 충돌이 드물게 발생하는 환경에서는 락을 사용하지 않으므로 높은 성능을 발휘할 수 있다.
    이는 락을 사용하는 방식과 비교했을 때 스레드가 락을 획득하기 위해 대기하지 않기 때문에 대기 시간과 오버헤드가 줄어드는 장점이 있다.

    그러나 충돌이 빈번하게 발생하는 환경에서는 성능에 문제가 될 수 있다.
    여러 스레드가 자주 동시에 동일한 변수의 값을 변경하려고 시도할 때 CAS 는 자주 실패하고 재시도해야 하므로 성능 저하가 발생할 수 있다.
    이런 상황에서는 반복문을 계속 돌기 때문에 CPU 자원을 많이 소모하게 된다.

    락(Lock) 방식
        - 비관적(pessimistic) 접근법
        - 데이터에 접근하기 전에 항상 락을 획득
        - 다른 스레드의 접근을 막음
        - "다른 스레드가 방해할 것이다"라고 가정
    CAS(Compare-And-Swap) 방식
        - 낙관적(optimistic) 접근법
        - 락을 사용하지 않고 데이터에 바로 접근
        - 충돌이 발생하면 그때 재시도
        - "대부분의 경우 충돌이 없을 것이다"라고 가정
    */
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println("start value : " + atomicInteger.get());

        List<Thread> list = new ArrayList<>();
        for(int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> incrementAndGet(atomicInteger));
            list.add(thread);
            thread.start();
        }

        for(Thread thread : list) {
            thread.join();
        }

        int i = atomicInteger.get();
        System.out.println(atomicInteger.getClass().getSimpleName() + ".result : " + i);
    }

    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;
        boolean result;

        do {
            getValue = atomicInteger.get();
            MyLogger.log("getValue : " + getValue);
            result = atomicInteger.compareAndSet(getValue, getValue + 1);
            MyLogger.log("result : " + result);
        } while(!result);

        return getValue + 1;
    }
}
