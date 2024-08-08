package thread.sync.lock;

import util.MyLogger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockEx {
    private final Lock nonFairLock;

    private final Lock fairLock;

    public ReentrantLockEx() {
        //비공정 모드 락
        this.nonFairLock = new ReentrantLock();
        //공정 모드 락
        this.fairLock = new ReentrantLock(true);
    }

    public void nonFairLockTest() {
        nonFairLock.lock();
        try {
            //임계 영역
            MyLogger.log("NonFairLock");
        } finally {
            nonFairLock.unlock();
        }
    }

    public void fairLockTest() {
        fairLock.lock();
        try {
            //임계 영역
            MyLogger.log("FairLock");
        } finally {
            fairLock.unlock();
        }
    }
}
