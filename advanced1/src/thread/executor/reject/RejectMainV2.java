package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectMainV2 {
    public static void main(String[] args) {
        //거절된 작업을 무시하고 아무런 예외도 발생시키지 않는다.
        //DiscardPolicy 의 rejectedExecution() 을 보면 비어있기 때문에 조용히 버리는 정책이다.
        ExecutorService es = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS
                , new SynchronousQueue<>(), new ThreadPoolExecutor.DiscardPolicy());

        es.submit(new RunnableTask("task1"));
        es.submit(new RunnableTask("task2"));
        es.submit(new RunnableTask("task3"));

        es.close();
    }
}
