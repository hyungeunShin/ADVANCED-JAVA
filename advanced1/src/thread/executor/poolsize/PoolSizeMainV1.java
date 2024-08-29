package thread.executor.poolsize;

import thread.executor.RunnableTask;
import util.ThreadUtils;

import java.util.concurrent.*;

import static thread.executor.util.ExecutorUtils.printState;
import static util.MyLogger.log;

public class PoolSizeMainV1 {
    /*
    Executor 스레드 풀 관리
        1. 작업을 요청하면 core 사이즈 만큼 스레드를 만든다.
        2. core 사이즈를 초과하면 큐에 작업을 넣는다.
        3. 큐를 초과하면 max 사이즈 만큼 스레드를 만든다. 임시로 사용되는 초과 스레드가 생성된다.
            - 큐가 가득차서 큐에 넣을 수도 없다. 초과 스레드가 바로 수행해야 한다.
        4. max 사이즈를 초과하면 요청을 거절한다. 예외가 발생한다.
            - 큐도 가득차고 풀최대 생성 가능한 스레드 수도 가득 찼다. 작업을 받을 수 없다.
    */

    public static void main(String[] args) {
        /*
        - 작업을 보관할 블로킹 큐의 구현체로 ArrayBlockingQueue(2) 를 사용했다. 사이즈를 2로 설정했으므로 최대 2개까지 작업을 큐에 보관할 수 있다.
        - corePoolSize=2, maximumPoolSize=4 를 사용해서 기본 스레드는 2개, 최대 스레드는 4개로 설정했다.
            - 스레드 풀에 기본 2개의 스레드를 운영한다. 요청이 너무 많거나 급한 경우 스레드 풀은 최대 4개까지 스레드를 증가시켜서 사용할 수 있다.
              이렇게 기본 스레드 수를 초과해서 만들어진 스레드를 초과 스레드라 하겠다.
        - 3000, TimeUnit.MILLISECONDS
            - 초과 스레드가 생존할 수 있는 대기 시간을 뜻한다. 이 시간 동안 초과 스레드가 처리할 작업이 없다면 초과 스레드는 제거된다.
            - 여기서는 3000 밀리초(3초)를 설정했으므로 초과 스레드가 3초간 작업을 하지 않고 대기한다면 초과 스레드는 스레드 풀에서 제거된다.
        */
        ExecutorService es = new ThreadPoolExecutor(2, 4, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));
        printState(es);

        es.execute(new RunnableTask("task1"));
        printState(es, "task1");

        es.execute(new RunnableTask("task2"));
        printState(es, "task2");

        es.execute(new RunnableTask("task3"));
        printState(es, "task3");

        es.execute(new RunnableTask("task4"));
        printState(es, "task4");

        es.execute(new RunnableTask("task5"));
        printState(es, "task5");

        es.execute(new RunnableTask("task6"));
        printState(es, "task6");

        try {
            es.execute(new RunnableTask("task7"));
        } catch(RejectedExecutionException e) {
            log("task7 실행 거절 예외 발생 : " + e);
        }

        ThreadUtils.sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        ThreadUtils.sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
        printState(es);
    }
}
