package thread.start;

public class DaemonThreadMain {
    /*
    데몬 스레드
        스레드는 사용자(user) 스레드와 데몬(daemon) 스레드 2가지 종류로 구분할 수 있다.

        - 사용자 스레드(non-daemon 스레드)
            - 프로그램의 주요 작업을 수행한다.
            - 작업이 완료될 때까지 실행된다.
            - 모든 user 스레드가 종료되면 JVM 도 종료된다.

        - 데몬 스레드
            - 백그라운드에서 보조적인 작업을 수행한다.
            - 모든 user 스레드가 종료되면 데몬 스레드는 자동으로 종료된다.
            - JVM 은 데몬 스레드의 실행 완료를 기다리지 않고 종료된다. 데몬 스레드가 아닌 모든 스레드가 종료되면 자바 프로그램도 종료된다.
    */

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        DaemonThread thread = new DaemonThread();
        /*
        데몬 스레드 여부는 start() 실행 전에 결정해야 한다. 이후에는 변경되지 않는다.
        기본 값은 false 이다.

        true 일 때
            main 스레드가 종료되면서 자바 프로그램도 종료된다.
            따라서 run() end 가 출력되기 전에 프로그램이 종료된다.
        false 일 때
            main 스레드가 종료되어도 user 스레드인 Thread-0 가 종료될 때 까지 자바 프로그램이 종료되지 않는다.
            따라서 Thread-0: run() end 가 출력된다.
            user 스레드인 main 스레드와 Thread-0 스레드가 모두 종료되면서 자바 프로그램도 종료된다.
        */
        thread.setDaemon(true); //데몬 스레드 여부
        thread.start();

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }

    static class DaemonThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": run() start");

            //run() 메서드 안에서 Thread.sleep() 를 호출할 때 체크 예외인 InterruptedException 을 밖으로 던질 수 없고 반드시 잡아야 한다.
            try {
                Thread.sleep(10000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + ": run() end");
        }
    }
}
