package thread.sync.lock;

public class ReentrantLockMain {
    public static void main(String[] args) {
        ReentrantLockEx lock = new ReentrantLockEx();

        //Thread t1 = new Thread(lock::nonFairLockTest, "t1");
        //Thread t2 = new Thread(lock::nonFairLockTest, "t2");
        //Thread t3 = new Thread(lock::nonFairLockTest, "t3");

        Thread t1 = new Thread(lock::fairLockTest, "t1");
        Thread t2 = new Thread(lock::fairLockTest, "t2");
        Thread t3 = new Thread(lock::fairLockTest, "t3");

        t1.start();
        t2.start();
        t3.start();
    }
}
