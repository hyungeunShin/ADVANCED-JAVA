package thread.visibility;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("flag : " + task.flag);
        t.start();

        sleep(1000);
        log("flag를 false로 변경 시도");
        task.flag = false;
        log("flag : " + task.flag);
        log("main 종료");
    }

    static class MyTask implements Runnable {
        //boolean flag = true;
        volatile boolean flag = true;

        @Override
        public void run() {
            log("task 시작");
            while(flag) {

            }
            log("task 종료");
        }
    }
}
