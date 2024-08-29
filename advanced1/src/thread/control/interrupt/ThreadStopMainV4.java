package thread.control.interrupt;

import util.ThreadUtils;

import static util.MyLogger.log;

public class ThreadStopMainV4 {
    /*
    스레드의 인터럽트 상태를 단순히 확인만 하는 용도라면 isInterrupted() 를 사용하면 된다.

    Thread.interrupted()
        - 스레드가 인터럽트 상태라면 true 를 반환하고 해당 스레드의 인터럽트 상태를 false 로 변경한다.
        - 스레드가 인터럽트 상태가 아니라면 false 를 반환하고 해당 스레드의 인터럽트 상태를 변경하지 않는다.

    Thread.interrupted() 를 호출했을 때 스레드가 인터럽트 상태(true)라면 true 를 반환하고 해당 스레드의 인터럽트 상태를 false 로 변경한다.
    결과적으로 while 문을 탈출하는 시점에 스레드의 인터럽트 상태도 false 로 변경된다.
    work 스레드는 이후에 자원을 정리하는 코드를 실행하는데 이때 인터럽트의 상태는 false 이므로 인터럽트가 발생하는 sleep() 과 같은 코드를 수행해도 인터럽트가 발생하지 않는다.
    이후에 자원을 정상적으로 잘 정리하는 것을 확인할 수 있다.
    인터럽트의 상태를 직접 체크해서 사용하는 경우 Thread.interrupted() 를 사용하면 이런 부분이 해결된다.
    isInterrupted() 는 특정 스레드의 상태를 변경하지 않고 확인할 때 사용한다.
    */

    public static void main(String[] args) {
        Thread thread = new Thread(new MyTask(), "work");

        thread.start();
        ThreadUtils.sleep(10);
        log("작업 중단 지시");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 : " + thread.isInterrupted());
    }
    
    static class MyTask implements Runnable {
        @Override
        public void run() {
            while(!Thread.interrupted()) {
                log("작업 중");
            }

            log("work 스레드 인터럽트 상태2 : " + Thread.currentThread().isInterrupted());

            try {
                log("자원 정리 시도");
                Thread.sleep(1000);
                log("자원 정리 완료");
            } catch(InterruptedException e) {
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 : " + Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }
}
