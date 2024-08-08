package thread.cas.advanced;

import util.MyLogger;

import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV2 {
    /*
    incrementAndGet() 메서드가 어떻게 CAS 연산을 활용해서 락 없이 만들어졌는지 직접 구현
    지금은 main 스레드 하나로 순서대로 실행되기 때문에 CAS 연산이 실패하지 않는다.
    */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println("start value : " + atomicInteger.get());

        int i1 = incrementAndGet(atomicInteger);
        System.out.println("resultValue1 : " + i1);
        int i2 = incrementAndGet(atomicInteger);
        System.out.println("resultValue2 : " + i2);
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
