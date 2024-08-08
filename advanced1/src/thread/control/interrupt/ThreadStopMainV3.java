package thread.control.interrupt;

import util.ThreadUtils;

import static util.MyLogger.log;

public class ThreadStopMainV3 {
    /*
    ThreadStopMainV2 에서 아쉬운 부분이 있다.
    while 의 조건이 true 이기 때문에 인터럽트가 발생해도 다음 코드로 넘어간다.
    그러고 sleep() 을 만나고 나서야 인터럽트가 발생하는 것이다.
    while 의 조건으로 인터럽트의 상태를 확인하면 더 빨리 반응할 수 있을 것이다.
    이렇게 하면 while 문을 체크하는 부분에서 더 빠르게 while 문을 빠져나갈 수 있다.
    물론 이 예제의 경우 코드가 단순해서 실질적인 차이는 매우 작다.

    ThreadStopMainV3 에는 심각한 문제가 있다.
    바로 work 스레드의 인터럽트 상태가 true 로 계속 유지된다는 점이다.
    앞서 인터럽트 예외가 터진 경우 스레드의 인터럽트 상태는 false 가 된다.
    반면에 isInterrupted() 메서드는 인터럽트의 상태를 변경하지 않는다. 단순히 인터럽트의 상태를 확인만 한다.
    work 스레드는 이후에 자원을 정리하는 코드를 실행하는데 이때도 인터럽트의 상태는 계속 true 로 유지된다.
    이때 만약 인터럽트가 발생하는 sleep() 과 같은 코드를 수행한다면 해당 코드에서 인터럽트 예외가 발생하게 된다.
    이것은 우리가 기대한 결과가 아니다!
    우리가 기대하는 것은 while() 문을 탈출하기 위해 딱 한 번만 인터럽트를 사용하는 것이지 다른 곳에서도 계속해서 인터럽트가 발생하는 것이 아니다.
    결과적으로 자원 정리를 하는 도중에 인터럽트가 발생해서 자원 정리에 실패한다.
    자바에서 인터럽트 예외가 한 번 발생하면 스레드의 인터럽트 상태를 다시 정상(false)으로 돌리는 것은 이런 이유 때문이다.
    스레드의 인터럽트 상태를 정상으로 돌리지 않으면 이후에도 계속 인터럽트가 발생하게 된다.
    인터럽트의 목적을 달성하면 인터럽트 상태를 다시 정상으로 돌려두어야 한다.

    while(인터럽트_상태_확인) 같은 곳에서 인터럽트의 상태를 확인하여 루프를 빠져나간 다음에 인터럽트 상태가 true 라면 인터럽트 상태를 다시 정상(false)으로 되돌릴려면 어떻게 해야할까?
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
            while(!Thread.currentThread().isInterrupted()) {
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
