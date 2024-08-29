package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectMainV3 {
    public static void main(String[] args) {
        /*
        호출한 스레드가 직접 작업을 수행하게 한다. 이로 인해 새로운 작업을 제출하는 스레드의 속도가 느려질 수 있다.
        이 정책의 특징은 생산자 스레드가 소비자 대신 일을 수행하는 것도 있지만 생산자 스레드가 대신 일을 수행하는 덕분에 작업의 생산 자체가 느려진다는 점이다.
        덕분에 작업의 생산 속도가 너무 빠르다면 생산 속도를 조절할 수 있다.
        원래대로 하면 main 스레드가 task1, task2, task3, task4 를 연속해서 바로 생산해야 한다.
        CallerRunsPolicy 정책 덕분에 main 스레드는 task2 를 본인이 직접 완료하고 나서야 task3 을 생산할 수 있다.
        결과적으로 생산 속도가 조절되었다.

        [CallerRunsPolicy]
            if(!e.isShutdown()) {
                r.run();
            }

        CallerRunsPolicy 의 rejectedExecution() 을 보면 r.run() 을 확인할 수 있다.
        여기서의 r은 RunnableTask 이다.
        별도의 스레드에서 수행하는 것이 아니라 main 스레드가 직접 수행하는 것을 알 수 있다.

        참고로 ThreadPoolExecutor 를 shutdown() 을 하면 이후에 요청하는 작업을 거절하는데 이때도 같은 정책이 적용된다.
        그런데 CallerRunsPolicy 정책은 shutdown() 이후에도 작업을 수행해버린다.
        따라서 shutdown() 조건을 체크해서 이 경우에는 작업을 수행하지 않도록 한다.
        */
        ExecutorService es = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS
                , new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        es.submit(new RunnableTask("task1"));
        es.submit(new RunnableTask("task2"));
        es.submit(new RunnableTask("task3"));
        es.submit(new RunnableTask("task4"));

        es.close();
    }
}
