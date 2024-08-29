package thread.executor.future;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class SumTaskMainV2 {
    /*
    - Future 라는 개념이 없다면 결과를 받을 때 까지 요청 스레드는 아무일도 못하고 대기해야 한다.
      따라서 다른 작업을 동시에 수행할 수도 없다.
    - Future 라는 개념 덕분에 요청 스레드는 대기하지 않고 다른 작업을 수행할 수 있다.
      예를 들어서 다른 작업을 더 요청할 수 있다. 그리고 모든 작업 요청이 끝난 다음에 본인이 필요할 때 Future.get() 을 호출해서 최종 결과를 받을 수 있다.
    - Future 를 사용하는 경우 결과적으로 task1, task2 를 동시에 요청할 수 있다.
      두 작업을 바로 요청했기 때문에 작업을 동시에 제대로 수행할 수 있다.

    Future 는 요청 스레드를 블로킹(대기) 상태로 만들지 않고 필요한 요청을 모두 수행할 수 있게 해준다.
    필요한 모든 요청을 한 다음에 Future.get() 을 통해 블로킹 상태로 대기하며 결과를 받으면 된다.
    이런 이유로 ExecutorService 는 결과를 직접 반환하지 않고 Future 를 반환한다.
    */

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        try(ExecutorService es = Executors.newFixedThreadPool(2)) {
            Future<Integer> future1 = es.submit(new SumTask(1, 50));    //non-blocking
            Future<Integer> future2 = es.submit(new SumTask(51, 100));  //non-blocking

            Integer sum1 = future1.get();   //blocking
            Integer sum2 = future2.get();   //blocking

            log("task1.result : " + sum1);
            log("task2.result : " + sum2);
            log("task1 + task2 : " + (sum1 + sum2));
            log("end");
        }
    }

    static class SumTask implements Callable<Integer> {
        int start;
        int end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() {
            log("작업 시작");
            sleep(2000);
            int sum = IntStream.rangeClosed(start, end).sum();
            log("작업 완료 result : " + sum);
            return sum;
        }
    }
}
