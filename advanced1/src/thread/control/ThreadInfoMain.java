package thread.control;

import thread.start.HelloRunnable;

import static util.MyLogger.log;

public class ThreadInfoMain {
    /*
    스레드 객체 정보
        스레드 ID, 스레드 이름, 우선순위, 스레드 그룹

    threadId() - 스레드 ID
        스레드의 고유 식별자를 반환하는 메서드이다. 이 ID는 JVM 내에서 각 스레드에 대해 유일하다.
        ID는 스레드가 생성될 때 할당되며 직접 지정할 수 없다.

    getName() - 스레드 이름
        스레드의 이름을 반환하는 메서드이다. 스레드 ID는 중복되지 않지만 스레드 이름은 중복될 수 있다.

    getPriority() - 스레드 우선순위
        스레드의 우선순위를 반환하는 메서드이다.
        우선순위는 1 (가장 낮음)에서 10 (가장 높음)까지의 값으로 설정할 수 있으며 기본값은 5이다.
        setPriority() 메서드를 사용해서 우선순위를 변경할 수 있다.
        우선순위는 스레드 스케줄러가 어떤 스레드를 우선 실행할지 결정하는 데 사용된다. 하지만 실제 실행 순서는 JVM 구현과 운영체제에 따라 달라질 수 있다.

    getThreadGroup() - 스레드 그룹
        스레드가 속한 스레드 그룹을 반환하는 메서드이다.
        스레드 그룹은 스레드를 그룹화하여 관리할 수 있는 기능을 제공한다.
        기본적으로 모든 스레드는 부모 스레드와 동일한 스레드 그룹에 속하게 된다.
        스레드 그룹은 여러 스레드를 하나의 그룹으로 묶어서 특정 작업(예: 일괄 종료, 우선순위 설정 등)을 수행할 수 있다.

        부모 스레드(Parent Thread)
            새로운 스레드를 생성하는 스레드를 의미한다. 스레드는 기본적으로 다른 스레드에 의해 생성된다.
            이러한 생성 관계에서 새로 생성된 스레드는 생성한 스레드를 부모로 간주한다.
            예를 들어 myThread 는 main 스레드에 의해 생성되었으므로 main 스레드가 부모 스레드이다.
            main 스레드는 기본으로 제공되는 main 스레드 그룹에 소속되어 있다.
            따라서 myThread 도 부모 스레드인 main 스레드의 그룹인 main 스레드 그룹에 소속된다.

    getState() - 스레드 상태
        스레드의 현재 상태를 반환하는 메서드이다. 반환되는 값은 Thread.State 열거형에 정의된 상수 중 하나이다.
            - NEW: 스레드가 아직 시작되지 않은 상태이다.
            - RUNNABLE: 스레드가 실행 중이거나 실행될 준비가 된 상태이다.
            - BLOCKED: 스레드가 동기화 락을 기다리는 상태이다.
            - WAITING: 스레드가 다른 스레드의 특정 작업이 완료되기를 기다리는 상태이다.
            - TIMED_WAITING: 일정 시간 동안 기다리는 상태이다.
            - TERMINATED: 스레드가 실행을 마친 상태이다.
    */
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        log("mainThread : " + mainThread);
        log("mainThread.threadId() : " + mainThread.threadId());
        log("mainThread.getName() : " + mainThread.getName());
        log("mainThread.getPriority() : " + mainThread.getPriority());
        log("mainThread.getThreadGroup() : " + mainThread.getThreadGroup());
        log("mainThread.getState() : " + mainThread.getState());

        Thread thread = new Thread(new HelloRunnable(), "myThread");
        log("mainThread : " + thread);
        log("mainThread.threadId() : " + thread.threadId());
        log("mainThread.getName() : " + thread.getName());
        log("mainThread.getPriority() : " + thread.getPriority());
        log("mainThread.getThreadGroup() : " + thread.getThreadGroup());
        log("mainThread.getState() : " + thread.getState());
    }
}
