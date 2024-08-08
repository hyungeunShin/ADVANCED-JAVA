package thread.control.join;

import java.util.stream.IntStream;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV3 {
    /*
    thread-1 이 아직 종료되지 않았다면 main 스레드는 thread1.join() 코드 안에서 더는 진행하지 않고 멈추어 기다린다.
    이후에 thread-1 이 종료되면 main 스레드는 RUNNABLE 상태가 되고 다음 코드로 이동한다.
    이때 thread-2 이 아직 종료되지 않았다면 main 스레드는 thread2.join() 코드 안에서 진행하지 않고 멈추어 기다린다.
    이후에 thread-2 이 종료되면 main 스레드는 RUNNABLE 상태가 되고 다음 코드로 이동한다.
    이 경우 thread-1 이 종료되는 시점에 thread-2 도 거의 같이 종료되기 때문에 thread2.join() 은 대기하지 않고 바로 빠져나온다.

    하지만 join() 의 단점은 다른 스레드가 완료될 때 까지 무기한 기다리는 단점이 있다.
    비유를 하자면 맛집에 한 번 줄을 서면 중간에 포기하지 못하고 자리가 날 때 까지 무기한 기다려야 한다.
    만약 다른 스레드의 작업을 일정 시간 동안만 기다리고 싶다면 어떻게 해야할까?
    */

    public static void main(String[] args) throws InterruptedException {
        log("start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");
        thread1.start();
        thread2.start();

        log("main 스레드가 thread1, thread2 종료까지 대기");
        //이때 main 스레드는 WAITING 상태가 된다.
        thread1.join();
        thread2.join();
        log("main 스레드 대기 완료");

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
