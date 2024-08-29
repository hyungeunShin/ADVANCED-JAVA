package thread.visibility;

public class AJavaMemoryModel {
    /*
    자바 메모리 모델(Java Memory Model)
        메모리 가시성(memory visibility)
            멀티스레드 환경에서 한 스레드가 변경한 값이 다른 스레드에서 언제 보이는지에 대한 것을 메모리 가시성(memory visibility)이라 한다.
            이름 그대로 메모리에 변경한 값이 보이는가 보이지 않는가의 문제이다.

    Java Memory Model
        Java Memory Model(JMM)은 자바 프로그램이 어떻게 메모리에 접근하고 수정할 수 있는지를 규정하며 특히 멀티스레드 프로그래밍에서 스레드 간의 상호작용을 정의한다.
        JMM 에 여러가지 내용이 있지만 핵심은 여러 스레드들의 작업 순서를 보장하는 happens-before 관계에 대한 정의다.

    happens-before
        happens-before 관계는 자바 메모리 모델에서 스레드 간의 작업 순서를 정의하는 개념이다.
        만약 A 작업이 B 작업보다 happens-before 관계에 있다면 A 작업에서의 모든 메모리 변경 사항은 B 작업에서 볼 수 있다.
        즉, A 작업에서 변경된 내용은 B 작업이 시작되기 전에 모두 메모리에 반영된다.

        - happens-before 관계는 이름 그대로 한 동작이 다른 동작보다 먼저 발생함을 보장한다.
        - happens-before 관계는 스레드 간의 메모리 가시성을 보장하는 규칙이다.
        - happens-before 관계가 성립하면 한 스레드의 작업을 다른 스레드에서 볼 수 있게 된다.
        - 한 스레드에서 수행한 작업을 다른 스레드가 참조할 때 최신 상태가 보장되는 것이다.

        이 규칙을 따르면 프로그래머가 멀티스레드 프로그램을 작성할 때 예상치 못한 동작을 피할 수 있다.

    happens-before 관계가 발생하는 경우
        프로그램 순서 규칙
            단일 스레드 내에서 프로그램의 순서대로 작성된 모든 명령문은 happens-before 순서로 실행된다.
            예를 들어 int a = 1; int b = 2; 에서 a = 1 은 b = 2 보다 먼저 실행된다.

        volatile 변수 규칙
            한 스레드에서 volatile 변수에 대한 쓰기 작업은 해당 변수를 읽는 모든 스레드에 보이도록 한다.
            즉, volatile 변수에 대한 쓰기 작업은 그 변수를 읽는 작업보다 happens-before 관계를 형성한다.

        스레드 시작 규칙
            한 스레드에서 Thread.start() 를 호출하면 해당 스레드 내의 모든 작업은 start() 호출 이후에 실행된 작업보다 happens-before 관계가 성립한다.
            Thread t = new Thread(task);
            t.start();
            여기에서 start() 호출 전에 수행된 모든 작업은 새로운 스레드가 시작된 후의 작업보다 happens-before 관계를 가진다.

        스레드 종료 규칙
            한 스레드에서 Thread.join() 을 호출하면 join 대상 스레드의 모든 작업은 join() 이 반환된 후의 작업보다 happens-before 관계를 가진다.
            예를 들어 thread.join() 호출 전에 thread 의 모든 작업이 완료되어야 하며 이 작업은 join() 이 반환된 후에 참조 가능하다.
            1 ~ 100 까지 값을 더하는 sumTask 예시를 떠올려보자.

        인터럽트 규칙
            한 스레드에서 Thread.interrupt() 를 호출하는 작업이 인터럽트된 스레드가 인터럽트를 감지하는 시점의 작업보다 happens-before 관계가 성립한다.
            즉, interrupt() 호출 후 해당 스레드의 인터럽트 상태를 확인하는 작업이 happens-before 관계에 있다.
            만약 이런 규칙이 없다면 인터럽트를 걸어도 한참 나중에 인터럽트가 발생할 수 있다.

        객체 생성 규칙
            객체의 생성자는 객체가 완전히 생성된 후에만 다른 스레드에 의해 참조될 수 있도록 보장한다.
            즉, 객체의 생성자에서 초기화된 필드는 생성자가 완료된 후 다른 스레드에서 참조될 때 happens-before 관계가 성립한다.

        모니터 락 규칙
            한 스레드에서 synchronized 블록을 종료한 후 그 모니터 락을 얻는 모든 스레드는 해당 블록 내의 모든 작업을 볼 수 있다.
            예를 들어 synchronized(lock) { ... } 블록 내에서의 작업은 블록을 나가는 시점에 happens-before 관계가 형성된다.
            뿐만 아니라 ReentrantLock 과 같이 락을 사용하는 경우에도 happens-before 관계가 성립한다.

        전이 규칙 (Transitivity Rule)
            만약 A가 B보다 happens-before 관계에 있고 B가 C보다 happens-before 관계에 있다면 A는 C보다 happens-before 관계에 있다.

    volatile 또는 스레드 동기화 기법(synchronized, ReentrantLock)을 사용하면 메모리 가시성의 문제가 발생하지 않는다.
    */
}
