package thread.start;

public class BadThreadMain {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread thread = new HelloThread();

        System.out.println(Thread.currentThread().getName() + ": start() 전");
        /*
        실행 결과를 잘 보면 별도의 스레드가 run() 을 실행하는 것이 아니라 main 스레드가 run() 메서드를 호출한 것을 확인할 수 있다.
        자바를 처음 실행하면 main 스레드가 main() 메서드를 호출하면서 시작한다.
        main 스레드는 HelloThread 인스턴스에 있는 run() 이라는 메서드를 호출한다.
        main 스레드가 run() 메서드를 실행했기 때문에 main 스레드가 사용하는 스택위에 run() 스택 프레임이 올라간다.
        결과적으로 main 스레드에서 모든 것을 처리한 것이 된다.

        스레드의 start() 메서드는 스레드에 스택 공간을 할당하면서 스레드를 시작하는 아주 특별한 메서드이다.
        그리고 해당 스레드에서 run() 메서드를 실행한다.
        따라서 main 스레드가 아닌 별도의 스레드에서 재정의한 run() 메서드를 실행하려면 반드시 start() 메서드를 호출해야 한다.
        */
        thread.run();
        System.out.println(Thread.currentThread().getName() + ": start() 후");

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
