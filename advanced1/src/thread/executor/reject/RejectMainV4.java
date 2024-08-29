package thread.executor.reject;

import thread.executor.RunnableTask;
import util.MyLogger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RejectMainV4 {
    public static void main(String[] args) {
        /*
        사용자는 RejectedExecutionHandler 인터페이스를 구현하여 자신만의 거절 처리 전략을 정의할 수 있다.
        이를 통해 특정 요구사항에 맞는 작업 거절 방식을 설정할 수 있다.
        */
        ExecutorService es = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS
                , new SynchronousQueue<>(), new MyRejectedExecutionHandler());

        es.submit(new RunnableTask("task1"));
        es.submit(new RunnableTask("task2"));
        es.submit(new RunnableTask("task3"));

        es.close();
    }

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        static AtomicInteger count = new AtomicInteger();

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            int i = count.incrementAndGet();
            MyLogger.log("[경고] 거절된 누적 작업 수 : " + i);
        }
    }
}
