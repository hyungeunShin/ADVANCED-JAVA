package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class MyPrinterV1 {
    /*
    volatile
        여러 스레드가 동시에 접근하는 변수에는 volatile 키워드를 붙어주어야 안전하다.
        여기서는 main 스레드, printer 스레드 둘다 work 변수에 동시에 접근할 수 있다.

    ConcurrentLinkedQueue
        여러 스레드가 동시에 접근하는 경우 컬렉션 프레임워크가 제공하는 일반적인 자료구조를 사용하면 안전하지 않다.
        여러 스레드가 동시에 접근하는 경우 동시성을 지원하는 동시성 컬렉션을 사용해야 한다.

    MyPrinterV1 문제는 종료(q)를 입력했을 때 바로 반응하지 않는다는 점이다.
    왜냐하면 printer 스레드가 반복문을 빠져나오려면 while 문을 체크해야 하는데 printer 스레드가 sleep(3000) 을 통해 대기 상태에 빠져서 작동하지 않기 때문이다.
    따라서 최악의 경우 q 를 입력하고 3초 이후에 프린터가 종료된다.
    */

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread thread = new Thread(printer, "printer");
        thread.start();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            log("프린터 할 문서를 입력하세요. 종료 (q) : ");
            String input = scanner.nextLine();

            if("q".equals(input)) {
                printer.work = false;
                break;
            }

            printer.addJob(input);
        }
    }

    static class Printer implements Runnable {
        volatile boolean work = true;
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while(work) {
                if(jobQueue.isEmpty()) {
                    continue;
                }

                String job = jobQueue.poll();
                log("출력 시작 : " + job + ", 대기 문서 : " + jobQueue);
                sleep(3000);
                log("출력 완료 : " + job);
            }
            log("프린터 종료");
        }

        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}
