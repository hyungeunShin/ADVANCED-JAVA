package thread.executor.future;

import util.ThreadUtils;

import java.util.List;
import java.util.concurrent.*;

import static util.MyLogger.log;

public class InvokeAllMain {
    /*
    invokeAll()
        - <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
            - 모든 Callable 작업을 제출하고 모든 작업이 완료될 때까지 기다린다.
        - <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
            - 지정된 시간 내에 모든 Callable 작업을 제출하고 완료될 때까지 기다린다.
    */

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        try(ExecutorService es = Executors.newFixedThreadPool(10)) {
            CallableTask task1 = new CallableTask("task1", 1000);
            CallableTask task2 = new CallableTask("task2", 2000);
            CallableTask task3 = new CallableTask("task3", 3000);

            List<CallableTask> list = List.of(task1, task2, task3);
            List<Future<Integer>> futures = es.invokeAll(list);
            for(Future<Integer> f : futures) {
                Integer result = f.get();
                log("result : " + result);
            }
        }
    }

    static class CallableTask implements Callable<Integer> {
        private final String name;
        private final int sleepMs;

        public CallableTask(String name, int sleepMs) {
            this.name = name;
            this.sleepMs = sleepMs;
        }

        @Override
        public Integer call() {
            log(name + " 실행");
            ThreadUtils.sleep(sleepMs);
            log(name + " 완료, return : " + sleepMs);
            return sleepMs;
        }
    }
}
