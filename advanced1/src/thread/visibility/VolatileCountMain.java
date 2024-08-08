package thread.visibility;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMain {
    /*
    volatile 없을 때
        - main 스레드가 flag 를 false 로 변경한 시점에 count 값은 1000410441 이다.
        - work 스레드가 flag 값을 false 로 확인한 시점에 count 값은 1100000000 이다.
        결과적으로 main 스레드가 flag 값을 false 로 변경하고 한참이 지나서야 work 스레드는 flag 값이 false 로 변경된 것을 확인한 것이다.

    volatile 있을 때
        - main 스레드가 flag 를 변경하는 시점에 work 스레드도 flag 의 변경 값을 정확하게 확인할 수 있다.
        - volatile 을 적용하면 캐시 메모리가 아니라 메인 메모리에 항상 직접 접근하기 때문에 성능이 상대적으로 떨어진다.
            - volatile 이 없을 때: 1000410441
            - volatile 이 있을 때: 161796531
            - 둘을 비교해보면 물리적으로 몇배의 성능 차이를 확인할 수 있다. 성능은 환경에 따라 차이가 있다.
    */
    
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        t.start();

        sleep(1000);

        task.flag = false;
        log("flag : " + task.flag + ", count : " + task.count + " in main");
    }

    static class MyTask implements Runnable {
        boolean flag = true;
        long count;

        //volatile boolean flag = true;
        //volatile long count;

        @Override
        public void run() {
            while(flag) {
                count++;
                if(count % 100_000_000 == 0) {
                    log("flag : " + flag + ", count : " + count + " in while");
                }
            }
            log("flag : " + flag + ", count : " + count + " 종료");
        }
    }
}
