package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.util.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class PoolSizeMainV3 {
    /*
    모든 작업이 대기하지 않고 작업의 수 만큼 스레드가 생기면서 바로 실행되는 것을 확인할 수 있다.
    "maximumPoolSize 대기 시간 초과" 로그를 통해 초과 스레드가 대기 시간이 지나서 모두 사라진 것을 확인할 수 있다.

    이 전략은 다음과 같은 특징이 있다.

    특징
        캐시 스레드 풀 전략은 매우 빠르고 유연한 전략이다.
        이 전략은 기본 스레드도 없고 대기 큐에 작업도 쌓이지 않는다.
        대신에 작업 요청이 오면 초과 스레드로 작업을 바로바로 처리한다.
        따라서 빠른 처리가 가능하다.
        초과 스레드의 수도 제한이 없기 때문에 CPU, 메모리 자원만 허용한다면 시스템의 자원을 최대로 사용할 수 있다.
        추가로 초과 스레드는 60초간 생존하기 때문에 작업 수에 맞추어 적절한 수의 스레드가 재사용된다.
        이런 특징 때문에 요청이 갑자기 증가하면 스레드도 갑자기 증가하고 요청이 줄어들면 스레드도 점점 줄어든다.
        이 전략은 작업의 요청 수에 따라서 스레드도 증가하고 감소하므로 매우 유연한 전략이다.

    그런데 어떻게 기본 스레드 없이 초과 스레드만 만들 수 있을까?
        Executor 스레드 풀 관리
            1. 작업을 요청하면 core 사이즈 만큼 스레드를 만든다.
                - core 사이즈가 없다. 바로 core 사이즈를 초과한다.
            2. core 사이즈를 초과하면 큐에 작업을 넣는다.
                - 큐에 작업을 넣을 수 없다. (SynchronousQueue 는 큐의 저장 공간이 0인 특별한 큐이다.)
            3. 큐를 초과하면 max 사이즈 만큼 스레드를 만든다. 임시로 사용되는 초과 스레드가 생성된다.
                - 초과 스레드가 생성된다. 물론 풀에 대기하는 초과 스레드가 있으면 재사용된다.
            4. max 사이즈를 초과하면 요청을 거절한다. 예외가 발생한다.
                - 참고로 max 사이즈가 무제한이다. 따라서 초과 스레드를 무제한으로 만들 수 있다.
        결과적으로 이 전략의 모든 작업은 초과 스레드가 처리한다.

    주의
        이 방식은 작업 수에 맞추어 스레드 수가 변하기 때문에 작업의 처리 속도가 빠르다.
        또한 CPU, 메모리를 매우 유연하게 사용할 수 있다는 장점이 있다.
        하지만 상황에 따라서 장점이 가장 큰 단점이 되기도 한다.

        상황1 - 점진적인 사용자 확대
            - 개발한 서비스가 잘 되어서 사용자가 점점 늘어난다.
            - 캐시 스레드 전략을 사용하면 이런 경우 크게 문제가 되지 않는다.
            - 캐시 스레드 전략은 이런 경우에는 문제를 빠르게 찾을 수 있다.
              사용자가 점점 증가하면서 스레드 사용량도 함께 늘어난다. 따라서 CPU 메모리의 사용량도 자연스럽게 증가한다.
            - 물론 CPU, 메모리 자원은 한계가 있기 때문에 적절한 시점에 시스템을 증설해야 한다.
              그렇지 않으면 CPU, 메모리 같은 시스템 자원을 너무 많이 사용하면서 시스템이 다운될 수 있다.

        상황2 - 갑작스런 요청 증가
            - 마케팅 팀의 이벤트가 대성공 하면서 갑자기 사용자가 폭증했다.
            - 고객은 응답을 받지 못한다고 항의한다.

        상황2 - 확인
        - 개발자는 급하게 CPU, 메모리 사용량을 확인해보는데 CPU 사용량이 100%이고 메모리 사용량도 지나치게 높아져있다.
        - 스레드 수를 확인해보니 스레드가 수가 1000개 실행되고 있다. 너무 많은 스레드가 작업을 처리하면서 시스템 전체가 느려지는 현상이 발생한다.
        - 캐시 스레드 풀 전략은 스레드가 무한으로 생성될 수 있다.
        - 수 천개의 스레드가 처리하는 속도 보다 더 많은 작업이 들어온다.
        - 시스템은 너무 많은 스레드에 잠식 당해서 거의 다운된다. 메모리도 거의 다 사용되어 버린다.
        - 시스템이 멈추는 장애가 발생한다.
        - 고정 스레드 풀 전략은 서버 자원은 여유가 있는데 사용자만 점점 느려지는 문제가 발생할 수 있다.
          반면에 캐시 스레드 풀 전략은 서버의 자원을 최대한 사용하지만 서버가 감당할 수 있는 임계점을 넘는 순간 시스템이 다운될 수 있다.
    */

    public static void main(String[] args) {
        //ExecutorService es = Executors.newCachedThreadPool();
        //keepAliveTime 60초 -> 3초로 조절
        ExecutorService es = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3, TimeUnit.SECONDS, new SynchronousQueue<>());

        log("pool 생성");
        printState(es);

        for(int i = 1; i <= 4; i++) {
            String task = "task" + i;
            es.execute(new RunnableTask(task));
            printState(es, task);
        }

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
        printState(es);
    }
}
