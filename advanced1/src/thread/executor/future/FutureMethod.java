package thread.executor.future;

public class FutureMethod {
    /*
    주요 메서드

    boolean cancel(boolean mayInterruptIfRunning)
        - 기능: 아직 완료되지 않은 작업을 취소한다.
        - 매개변수: mayInterruptIfRunning
            - cancel(true) : Future 를 취소 상태로 변경한다. 이때 작업이 실행중이라면 Thread.interrupt() 를 호출해서 작업을 중단한다.
            - cancel(false) : Future 를 취소 상태로 변경한다. 단 이미 실행 중인 작업을 중단하지는 않는다.
        - 반환값: 작업이 성공적으로 취소된 경우 true, 이미 완료되었거나 취소할 수 없는 경우 false
        - 설명: 작업이 실행 중이 아니거나 아직 시작되지 않았으면 취소하고 실행 중인 작업의 경우 mayInterruptIfRunning 이 true 이면 중단을 시도한다.
        - 참고: 취소 상태의 Future 에 Future.get() 을 호출하면 CancellationException 런타임 예외가 발생한다.

    boolean isCancelled()
        - 기능: 작업이 취소되었는지 여부를 확인한다.
        - 반환값: 작업이 취소된 경우 true, 그렇지 않은 경우 false
        - 설명: 이 메서드는 작업이 cancel() 메서드에 의해 취소된 경우에 true 를 반환한다.

    boolean isDone()
        - 기능: 작업이 완료되었는지 여부를 확인한다.
        - 반환값: 작업이 완료된 경우 true, 그렇지 않은 경우 false
        - 설명: 작업이 정상적으로 완료되었거나, 취소되었거나, 예외가 발생하여 종료된 경우에 true 를 반환한다.

    State state()
        - 기능: Future 의 상태를 반환한다. 자바 19부터 지원한다.
            - RUNNING : 작업 실행 중
            - SUCCESS : 성공 완료
            - FAILED : 실패 완료
            - CANCELLED : 취소 완료

    V get()
        - 기능: 작업이 완료될 때까지 대기하고 완료되면 결과를 반환한다.
        - 반환값: 작업의 결과
        - 예외
            - InterruptedException : 대기 중에 현재 스레드가 인터럽트된 경우 발생
            - ExecutionException : 작업 계산 중에 예외가 발생한 경우 발생
        - 설명: 작업이 완료될 때까지 get() 을 호출한 현재 스레드를 대기(블록킹)한다. 작업이 완료되면 결과를 반환한다.

    V get(long timeout, TimeUnit unit)
        - 기능: get() 과 같은데 시간 초과되면 예외를 발생시킨다.
        - 매개변수
            - timeout : 대기할 최대 시간
            - unit : timeout 매개변수의 시간 단위 지정
        - 반환값: 작업의 결과
        - 예외
            - InterruptedException : 대기 중에 현재 스레드가 인터럽트된 경우 발생
            - ExecutionException : 계산 중에 예외가 발생한 경우 발생
            - TimeoutException : 주어진 시간 내에 작업이 완료되지 않은 경우 발생
        - 설명: 지정된 시간 동안 결과를 기다린다. 시간이 초과되면 TimeoutException 을 발생시킨다.
    */
}
