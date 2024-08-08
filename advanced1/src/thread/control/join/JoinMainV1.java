package thread.control.join;

import java.util.stream.IntStream;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV1 {
    /*
    thread-1, thread-2 가 계산을 완료해서 result 에 연산 결과를 담을 때 까지는 약 2초 정도의 시간이 걸린다.
    main 스레드는 계산이 끝나기 전에 result 의 결과를 조회한 것이다.
    따라서 0 값이 출력된다.
    여기서 문제의 핵심은 main 스레드가 thread-1, thread-2 의 계산이 끝날 때 까지 기다려야 한다는 점이다.
    */

    public static void main(String[] args) {
        log("start");

        /*
        this
            어떤 메서드를 호출하는 것은 정확히는 특정 스레드가 어떤 메서드를 호출하는 것이다.
            스레드는 메서드의 호출을 관리하기 위해 메서드 단위로 스택 프레임을 만들고 해당 스택 프레임을 스택위에 쌓아 올린다.
            이때 인스턴스의 메서드를 호출하면 어떤 인스턴스의 메서드를 호출했는지 기억하기 위해 해당 인스턴스의 참조값을 스택 프레임 내부에 저장해둔다.
            이것이 바로 우리가 자주 사용하던 this 이다.

            특정 메서드 안에서 this 를 호출하면 바로 스택프레임 안에 있는 this 값을 불러서 사용하게 된다.
            이렇게 this 가 있기 때문에 thread-1, thread-2는 자신의 인스턴스를 구분해서 사용할 수 있다.
            예를 들어서 필드에 접근할 때 this 를 생략하면 자동으로 this 를 참고해서 필드에 접근한다.

            정리하면 this 는 호출된 인스턴스 메서드가 소속된 객체를 가리키는 참조이며 이것이 스택 프레임 내부에 저장되어 있다.
        */
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");
        thread1.start();
        thread2.start();

        log("task1.result : " + task1.result);
        log("task2.result : " + task2.result);
        log("task1 + task2 : " + (task1.result + task2.result));

        log("end");
    }

    static class SumTask implements Runnable {
        int start;
        int end;
        int result;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000);
            result = IntStream.rangeClosed(start, end).sum();
            log("작업 완료 result : " + result);
        }
    }
}
