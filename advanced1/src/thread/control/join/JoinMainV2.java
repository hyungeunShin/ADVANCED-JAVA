package thread.control.join;

import java.util.stream.IntStream;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV2 {
    /*
    특정 스레드를 기다리게 하는 가장 간단한 방법은 sleep() 을 사용하는 것이다.
    하지만 이렇게 sleep() 을 사용해서 무작정 기다리는 방법은 대기 시간에 손해를 본다.
    또한 thread-1, thread-2 의 수행시간이 달라지는 경우에는 정확한 타이밍을 맞추기 어렵다.
    */

    public static void main(String[] args) {
        log("start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");
        thread1.start();
        thread2.start();

        log("main 스레드 sleep() start");
        sleep(3000);
        log("main 스레드 sleep() end");

        log("task1.result : " + task1.result);
        log("task2.result : " + task2.result);
        log("task1 + task2 : " + (task1.result + task2.result));

        log("end");
    }

    static class SumTask implements Runnable {
        int start;
        int end;
        int result;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000);
            result = IntStream.rangeClosed(start, end).sum();
            log("작업 완료 result : " + result);
        }
    }
}
