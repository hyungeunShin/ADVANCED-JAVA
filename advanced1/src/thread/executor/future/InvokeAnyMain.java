package thread.executor.future;

import util.ThreadUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

public class InvokeAnyMain {
    /*
    invokeAny()
        - <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
            - 하나의 Callable 작업이 완료될 때까지 기다리고 가장 먼저 완료된 작업의 결과를 반환한다.
            - 완료되지 않은 나머지 작업은 취소한다.
        - <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
            - 지정된 시간 내에 하나의 Callable 작업이 완료될 때까지 기다리고 가장 먼저 완료된 작업의 결과를 반환한다.
            - 완료되지 않은 나머지 작업은 취소한다.
    */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(ExecutorService es = Executors.newFixedThreadPool(10)) {
            CallableTask task1 = new CallableTask("task1", 1000);
            CallableTask task2 = new CallableTask("task2", 2000);
            CallableTask task3 = new CallableTask("task3", 3000);

            List<CallableTask> list = List.of(task1, task2, task3);
            Integer result = es.invokeAny(list);
            log("result : " + result);
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
