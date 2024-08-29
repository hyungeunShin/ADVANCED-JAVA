package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.util.ExecutorUtils.printState;
import static util.MyLogger.log;

public class PoolSizeMainV2 {
    /*
    2개의 스레드가 안정적으로 작업을 처리하는 것을 확인할 수 있다.
    이 전략은 다음과 같은 특징이 있다.

    특징
        스레드 수가 고정되어 있기 때문에 CPU, 메모리 리소스가 어느정도 예측 가능한 안정적인 방식이다.
        큐 사이즈도 제한이 없어서 작업을 많이 담아두어도 문제가 없다.

    주의
        이 방식의 가장 큰 장점은 스레드 수가 고정되어서 CPU, 메모리 리소스가 어느정도 예측 가능하다는 점이다.
        따라서 일반적인 상황에 가장 안정적으로 서비스를 운영할 수 있다. 하지만 상황에 따라 장점이 가장 큰 단점이 되기도 한다.

        상황1 - 점진적인 사용자 확대
            - 개발한 서비스가 잘 되어서 사용자가 점점 늘어난다.
            - 고정 스레드 전략을 사용해서 서비스를 안정적으로 잘 운영했는데 언젠가부터 사용자들이 서비스 응답이 점점 느려진다고 항의한다.

        상황2 - 갑작스런 요청 증가
            - 마케팅 팀의 이벤트가 대성공 하면서 갑자기 사용자가 폭증했다.
            - 고객은 응답을 받지 못한다고 항의한다.

        확인
            - 개발자는 급하게 CPU, 메모리 사용량을 확인해보는데 아무런 문제 없이 여유있게 안정적으로 서비스가 운영되고 있다.
            - 고정 스레드 전략은 실행되는 스레드 수가 고정되어 있다. 따라서 사용자가 늘어나도 CPU, 메모리 사용량이 확 늘어나지 않는다.
            - 큐의 사이즈를 확인해보니 요청이 수 만 건이 쌓여있다. 요청이 처리되는 시간보다 쌓이는 시간이 더 빠른 것이다.
              참고로 고정 풀 전략의 큐 사이즈는 무한이다.
            - 예를 들어서 큐에 10000건이 쌓여있는데 고정 스레드 수가 10이고 각 스레드가 작업을 하나 처리하는데 1초가 걸린다면 모든 작업을 다 처리하는데는 1000초가 걸린다.
              만약 처리 속도보다 작업이 쌓이는 속도가 더 빠른 경우에는 더 문제가 된다.
            - 서비스 초기에는 사용자가 적기 때문에 이런 문제가 없지만 사용자가 늘어나면 문제가 될 수 있다.
            - 갑작스런 요청 증가도 물론 마찬가지이다.

        결국 서버 자원은 여유가 있는데 사용자만 점점 느려지는 문제가 발생한 것이다.
    */

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(2);
        //ExecutorService es = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());

        log("pool 생성");
        printState(es);

        for(int i = 1; i <= 6; i++) {
            String task = "task" + i;
            es.execute(new RunnableTask(task));
            printState(es, task);
        }

        es.close();
        log("== shutdown 완료 ==");
    }
}
