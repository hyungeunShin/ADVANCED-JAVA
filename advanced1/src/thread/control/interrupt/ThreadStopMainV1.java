package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {
    /*
    특정 스레드의 작업을 중단하는 가장 쉬운 방법은 변수를 사용하는 것이다.
    여기서는 flag 를 사용해서 work 스레드에 작업 중단을 지시할 수 있다.

    main 스레드가 flag = false 를 통해 작업 중단을 지시해도 work 스레드가 즉각 반응하지 않는다.
    로그를 보면 작업 중단 지시 2초 정도 이후에 자원을 정리하고 작업을 종료한다.
    이 방식의 가장 큰 문제는 다음 코드의 sleep() 에 있다.
    main 스레드가 flag 를 false 로 변경해도 work 스레드는 sleep(3000) 을 통해 3초간 잠들어 있다.
    3초간의 잠이 깬 다음에 while(flag) 코드를 실행해야 flag 를 확인하고 작업을 중단할 수 있다.
    참고로 flag 를 변경한 후 2초라는 시간이 지난 이후에 작업이 종료되는 이유는 work 스레드가 3초에 한번씩 깨어나서 flag 를 확인하는데 main 스레드가 4초에 flag 를 변경했기 때문이다.
    work 스레드 입장에서 보면 두 번째 sleep() 에 들어가고 1초 후 main 스레드가 flag 를 변경한다.
    3초간 sleep() 이므로 아직 2초가 더 있어야 깨어난다.

    어떻게 하면 sleep() 처럼 스레드가 대기하는 상태에서 스레드를 깨우고 작업도 빨리 종료할 수 있을까?
    */

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");

        thread.start();
        sleep(4000);
        log("작업 중단 지시");
        task.flag = false;
    }

    static class MyTask implements Runnable {
        volatile boolean flag = true;

        @Override
        public void run() {
            while(flag) {
                log("작업 중");
                sleep(3000);
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
