package thread.start;

public class HelloThreadMain {
    /*
    스레드 생성 전
        실행 결과를 보면 main() 메서드는 main 이라는 이름의 스레드가 실행하는 것을 확인할 수 있다.
        프로세스가 작동하려면 스레드가 최소한 하나는 있어야 한다.
        그래야 코드를 실행할 수 있다. 자바는 실행 시점에 main 이라는 이름의 스레드를 만들고 프로그램의 시작점인 main() 메서드를 실행한다.

    스레드 생성 후
        - HelloThread 스레드 객체를 생성한 다음에 start() 메서드를 호출하면 자바는 스레드를 위한 별도의 스택 공간을 할당한다.
        - 스레드 객체를 생성하고, 반드시 start() 를 호출해야 스택 공간을 할당 받고 스레드가 작동한다.
        - 스레드에 이름을 주지 않으면 자바는 스레드에 Thread-0, Thread-1 과 같은 임의의 이름을 부여한다.
        - 새로운 Thread-0 스레드가 사용할 전용 스택 공간이 마련되었다.
        - Thread-0 스레드는 run() 메서드의 스택 프레임을 스택에 올리면서 run() 메서드를 시작한다.

    메서드를 실행하면 스택 위에 스택 프레임이 쌓인다
        - main 스레드는 main() 메서드의 스택 프레임을 스택에 올리면서 시작한다.
        - 직접 만드는 스레드는 run() 메서드의 스택 프레임을 스택에 올리면서 시작한다.

    스레드 간 실행 순서는 보장하지 않는다.
        스레드는 동시에 실행되기 때문에 스레드 간에 실행 순서는 얼마든지 달라질 수 있다.

        스레드 간의 실행 순서는 얼마든지 달라질 수 있다.
        CPU 코어가 2개여서 물리적으로 정말 동시에 실행될 수도 있고 하나의 CPU 코어에 시간을 나누어 실행될 수도 있다.
        그리고 한 스레드가 얼마나 오랜기간 실행되는지도 보장하지 않는다.
        한 스레드가 먼저 다 수행된 다음에 다른 스레드가 수행될 수도 있고 둘이 완전히 번갈아 가면서 수행되는 경우도 있다.
        스레드는 순서와 실행 기간을 모두 보장하지 않는다. 이것이 바로 멀티스레드다.
    */

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread thread = new HelloThread();
        
        System.out.println(Thread.currentThread().getName() + ": start() 전");
        /*
        start() 메서드는 스레드를 실행하는 아주 특별한 메서드이다.
        start() 를 호출하면 HelloThread 스레드가 run() 메서드를 실행한다.
        run() 메서드가 아니라 반드시 start() 메서드를 호출해야 한다. 그래야 별도의 스레드에서 run() 코드가 실행된다.

        여기서 핵심은 main 스레드가 run() 메서드를 실행하는게 아니라 Thread-0 스레드가 run() 메서드를 실행한다는 점이다.
        main 스레드는 단지 start() 메서드를 통해 Thread-0 스레드에게 실행을 지시할 뿐이다.
        다시 강조하지만 main 스레드가 run() 을 호출하는 것이 아니다.
        main 스레드는 다른 스레드에게 일을 시작하라고 지시만하고 바로 start() 메서드를 빠져나온다.
        이제 main 스레드와 Thread-0 스레드는 동시에 실행된다.
        main 스레드 입장에서 보면 그림의 1, 2, 3번 코드를 멈추지 않고 계속 수행한다.
        그리고 run() 메서드는 main 이 아닌 별도의 스레드에서 실행된다.
        */
        thread.start();
        System.out.println(Thread.currentThread().getName() + ": start() 후");
        
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
