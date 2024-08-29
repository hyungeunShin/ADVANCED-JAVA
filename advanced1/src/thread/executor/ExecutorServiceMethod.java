package thread.executor;

public class ExecutorServiceMethod {
    /*
    - void execute(Runnable command) : Runnable 작업을 제출한다. 반환값이 없다.

    - <T> Future<T> submit(Callable<T> task) : Callable 작업을 제출하고 결과를 반환받는다.

    - Future<?> submit(Runnable task) : Runnable 작업을 제출하고 결과를 반환받는다.
        - Runnable 은 반환 값이 없기 때문에 future.get() 을 호출할 경우 null 을 반환한다.
          결과가 없다 뿐이지 나머지는 똑같다. 작업이 완료될 때 까지 요청 스레드가 블로킹 되는 부분도 같다.

    - invokeAll()
        - <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
            - 모든 Callable 작업을 제출하고 모든 작업이 완료될 때까지 기다린다.
        - <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
            - 지정된 시간 내에 모든 Callable 작업을 제출하고 완료될 때까지 기다린다.

    - invokeAny()
        - <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
            - 하나의 Callable 작업이 완료될 때까지 기다리고 가장 먼저 완료된 작업의 결과를 반환한다.
            - 완료되지 않은 나머지 작업은 취소한다.
        - <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
            - 지정된 시간 내에 하나의 Callable 작업이 완료될 때까지 기다리고 가장 먼저 완료된 작업의 결과를 반환한다.
            - 완료되지 않은 나머지 작업은 취소한다.

    - void shutdown()
        - 새로운 작업을 받지 않고 이미 제출된 작업을 모두 완료한 후에 종료한다.
        - 논 블로킹 메서드(이 메서드를 호출한 스레드는 대기하지 않고 즉시 다음 코드를 호출한다.)

    - List<Runnable> shutdownNow()
        - 실행 중인 작업을 중단하고 대기 중인 작업을 반환하며 즉시 종료한다.
        - 실행 중인 작업을 중단하기 위해 인터럽트를 발생시킨다.
        - 논 블로킹 메서드

    - boolean isShutdown()
        - 서비스가 종료되었는지 확인한다.

    - boolean isTerminated()
        - shutdown(), shutdownNow() 호출 후 모든 작업이 완료되었는지 확인한다.

    - boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
        - 서비스 종료시 모든 작업이 완료될 때까지 대기한다. 이때 지정된 시간까지만 대기한다.
        - 블로킹 메서드

    - close()
        - close() 는 자바 19부터 지원하는 서비즈 종료 메서드이다. 이 메서드는 shutdown() 과 같다고 생각하면 된다.
        - 더 정확히는 shutdown() 을 호출하고 하루를 기다려도 작업이 완료되지 않으면 shutdownNow() 를 호출한다.
        - 호출한 스레드에 인터럽트가 발생해도 shutdownNow() 를 호출한다.
    */
}
