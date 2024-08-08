package thread.control.join;

import java.util.stream.IntStream;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {
    /*
    main 스레드는 join(1000) 을 사용해서 thread-1 을 1초간 기다린다.
    이때 main 스레드의 상태는 WAITING 이 아니라 TIMED_WAITING 이 된다.
    보통 무기한 대기하면 WAITING 상태가 되고 특정 시간 만큼만 대기하는 경우 TIMED_WAITING 상태가 된다.
    thread-1 의 작업에는 2초가 걸린다.
    1초가 지나도 thread-1 의 작업이 완료되지 않으므로 main 스레드는 대기를 중단한다.
    그리고 main 스레드는 다시 RUNNABLE 상태로 바뀌면서 다음 코드를 수행한다.
    이때 thread-1 의 작업이 아직 완료되지 않았기 때문에 task1.result = 0 이 출력된다.
    main 스레드가 종료된 이후에 thread-1 이 계산을 끝낸다.
    따라서 작업 완료 result = 1275 이 출력된다.
    */

    public static void main(String[] args) throws InterruptedException {
        log("start");

        SumTask task1 = new SumTask(1, 50);
        Thread thread1 = new Thread(task1, "thread-1");
        thread1.start();

        log("main 스레드가 thread1 종료까지 1초 대기");
        thread1.join(1000);
        log("main 스레드 대기 완료");

        log("task1.result : " + task1.result);

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
