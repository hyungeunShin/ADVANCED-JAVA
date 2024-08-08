package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV1 {
    /*
    대기 상태로 바꾸는 LockSupport.park() 는 매개변수가 없는데 실행 가능 상태로 바꾸는 LockSupport.unpark(thread1) 는 왜 특정 스레드를 지정하는 매개변수가 있을까?
    왜냐하면 실행 중인 스레드는 LockSupport.park() 를 호출해서 스스로 대기 상태에 빠질 수 있지만 대기 상태의 스레드는 자신의 코드를 실행할 수 없기 때문이다.
    따라서 외부 스레드의 도움을 받아야 깨어날 수 있다.
    */

    public static void main(String[] args) {
        Thread thread = new Thread(new ParkTask(), "Thread-1");
        thread.start();

        sleep(100);
        log("Thread-1 state : " + thread.getState());

        log("unpark(Thread-1)");
        //RUNNABLE -> WAITING
        LockSupport.unpark(thread);

        //RUNNABLE -> WAITING
        //WAITING 상태의 스레드는 인터럽트를 걸어서 중간에 깨울 수 있다.
        //thread.interrupt();
    }

    static class ParkTask implements Runnable {
        @Override
        public void run() {
            log("park 시작");
            LockSupport.park();
            log("park 종료");
            log("state : " + Thread.currentThread().getState());
            log("인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        }
    }
}
