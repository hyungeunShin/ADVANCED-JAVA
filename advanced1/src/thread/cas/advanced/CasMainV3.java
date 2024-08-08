package thread.cas.advanced;

import util.MyLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV3 {
    private static final int THREAD_COUNT = 2;

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
