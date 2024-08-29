package thread.cas.spinlock;

import util.MyLogger;
import util.ThreadUtils;

public class SpinLockMain {
    public static void main(String[] args) {
        //BadSpinLock spinLock = new BadSpinLock();
        SpinLock spinLock = new SpinLock();

        Runnable runnable = () -> {
            spinLock.lock();
            try {
                MyLogger.log("비즈니스 로직 실행");
                ThreadUtils.sleep(1);
            } finally {
                spinLock.unlock();
            }
        };

        Thread t1 = new Thread(runnable, "t1");
        Thread t2 = new Thread(runnable, "t2");
        t1.start();
        t2.start();
    }
}
