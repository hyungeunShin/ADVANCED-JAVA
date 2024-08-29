package thread.executor.reject;

public class AExecutorExceptionPolicy {
    /*
    Executor 예외 정책
        생산자 소비자 문제를 실무에서 사용할 때는 결국 소비자가 처리할 수 없을 정도로 생산 요청이 가득 차면 어떻게 할지를 정해야 한다.
        개발자가 인지할 수 있게 로그도 남겨야 하고 사용자에게 현재 시스템에 문제가 있다고 알리는 것도 필요하다.
        이런 것을 위해 예외 정책이 필요하다.

        ThreadPoolExecutor 에 작업을 요청할 때 큐도 가득차고 초과 스레드도 더는 할당할 수 없다면 작업을 거절한다.

        ThreadPoolExecutor 는 작업을 거절하는 다양한 정책을 제공한다.
            - AbortPolicy: 새로운 작업을 제출할 때 RejectedExecutionException 을 발생시킨다. 기본 정책이다.(기본정책이기 때문에 생략 가능)
            - DiscardPolicy: 새로운 작업을 조용히 버린다.
            - CallerRunsPolicy: 새로운 작업을 제출한 스레드가 대신해서 직접 작업을 실행한다.
            - 사용자 정의(RejectedExecutionHandler): 개발자가 직접 정의한 거절 정책을 사용할 수 있다.

        참고로 ThreadPoolExecutor 를 shutdown() 하면 이후에 요청하는 작업을 거절하는데 이때도 같은 정책이 적용된다.
    */
}
