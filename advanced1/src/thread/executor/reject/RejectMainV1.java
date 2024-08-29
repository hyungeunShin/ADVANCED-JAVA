package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class RejectMainV1 {
    public static void main(String[] args) {
        //기본 정책, RejectedExecutionException 이 발생한다.
        //AbortPolicy 는 RejectedExecutionHandler 의 구현체이다.
        //ThreadPoolExecutor 생성자는 RejectedExecutionHandler 의 구현체를 전달 받는다.
        ExecutorService es = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS
                , new SynchronousQueue<>(), new ThreadPoolExecutor.AbortPolicy());

        es.submit(new RunnableTask("task1"));

        try {
            es.submit(new RunnableTask("task2"));
        } catch(RejectedExecutionException e) {
            //RejectedExecutionException 예외를 잡아서 작업을 포기하거나 사용자에게 알리거나 다시 시도하면 된다.
            log("요청 초과");
            log(e);
        }

        es.close();
    }
}
