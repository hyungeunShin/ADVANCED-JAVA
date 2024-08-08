package thread.control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinterV4 {
    /*
    if(jobQueue.isEmpty()) {
        continue;
    }

    이 코드를 보면 인터럽트가 발생하기 전까지 계속 인터럽트의 상태를 체크하고 또 jobQueue 의 상태를 확인한다.
    문제는 쉴 틈 없이 CPU 에서 이 로직이 계속 반복해서 수행된다는 점이다.
    결과적으로 CPU 자원을 많이 사용하게 된다.

    현재 작동하는 스레드가 아주 많다고 가정해보자.
    인터럽트도 걸리지 않고 jobQueue 도 비어있는데 이런 체크 로직에 CPU 자원을 많이 사용하게 되면 정작 필요한 스레드들의 효율이 상대적으로 떨어질 수 있다.
    차라리 그 시간에 다른 스레드들을 더 많이 실행해서 jobQueue 에 필요한 작업을 빠르게 만들어 넣어주는게 더 효율적일 것이다.
    그래서 다음과 같이 jobQueue 에 작업이 비어있으면 yield() 를 호출해서 다른 스레드에 작업을 양보하는게 전체 관점에서 보면 더 효율적이다.
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
                thread.interrupt();
                break;
            }

            printer.addJob(input);
        }
    }

    static class Printer implements Runnable {
        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while(!Thread.interrupted()) {
                if(jobQueue.isEmpty()) {
                    Thread.yield();
                    continue;
                }

                try {
                    String job = jobQueue.poll();
                    log("출력 시작 : " + job + ", 대기 문서 : " + jobQueue);
                    Thread.sleep(3000);
                    log("출력 완료 : " + job);
                } catch(InterruptedException e) {
                    log("인터럽트 예외 발생");
                    break;
                }
            }
            log("프린터 종료");
        }

        public void addJob(String input) {
            jobQueue.offer(input);
        }
    }
}
