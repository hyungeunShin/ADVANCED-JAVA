package thread.control.interrupt;

import util.ThreadUtils;

import static util.MyLogger.log;

public class ThreadStopMainV2 {
    /*
    예를 들어서 특정 스레드가 Thread.sleep() 을 통해 쉬고 있는데 처리해야 하는 작업이 들어와서 해당 스레드를 급하게 깨워야 할 수 있다.
    또는 sleep() 으로 쉬고 있는 스레드에게 더는 일이 없으니 작업 종료를 지시할 수도 있다.
    인터럽트를 사용하면 WAITING, TIMED_WAITING 같은 대기 상태의 스레드를 직접 깨워서 RUNNABLE 상태로 만들 수 있다.

    - 특정 스레드의 인스턴스에 interrupt() 메서드를 호출하면 해당 스레드에 인터럽트가 발생한다.
    - 인터럽트가 발생하면 해당 스레드에 InterruptedException 이 발생한다.
        - 참고로 interrupt() 를 호출했다고 해서 즉각 InterruptedException 이 발생하는 것은 아니다.
          ★★★ 오직 sleep() 처럼 InterruptedException 을 던지는 메서드를 호출 하거나 또는 호출 중일 때 예외가 발생한다.
          예를 들어서 위 코드에서 while(true), log("작업 중") 에서는 InterruptedException 이 발생하지 않는다.
          Thread.sleep() 처럼 InterruptedException 을 던지는 메서드를 호출하거나 또는 호출하고 대기중일 때 예외가 발생한다.

        - 스레드가 인터럽트 상태일 때는 sleep() 처럼 InterruptedException 이 발생하는 메서드를 호출하거나 또는 이미 호출하고 대기 중이라면 InterruptedException 이 발생한다.
          work 스레드는 TIMED_WAITING 상태에서 RUNNABLE 상태로 변경되고 InterruptedException 예외를 처리하면서 반복문을 탈출한다.
          work 스레드는 인터럽트 상태가 되었고 인터럽트 상태이기 때문에 인터럽트 예외가 발생한다.
          인터럽트 상태에서 인터럽트 예외가 발생하면 work 스레드는 다시 동작하는 상태가 된다.
          따라서 work 스레드의 인터럽트 상태는 종료된다.
          work 스레드의 인터럽트 상태는 false 로 변경된다.
    - 인터럽트를 받은 스레드는 대기 상태에서 깨어나 RUNNABLE 상태가 되고 코드를 정상 수행한다.
    - InterruptedException 을 catch 로 잡아서 정상 흐름으로 변경하면 된다.
    */

    public static void main(String[] args) {
        Thread thread = new Thread(new MyTask(), "work");

        thread.start();
        ThreadUtils.sleep(4000);
        log("작업 중단 지시");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 : " + thread.isInterrupted());
    }
    
    static class MyTask implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    log("작업 중");
                    Thread.sleep(3000);
                }
            } catch(InterruptedException e) {
                log("work 스레드 인터럽트 상태2 : " + Thread.currentThread().isInterrupted());
                log("interrupt message : " + e.getMessage());
                log("work 스레트 상태 : " + Thread.currentThread().getState());
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
