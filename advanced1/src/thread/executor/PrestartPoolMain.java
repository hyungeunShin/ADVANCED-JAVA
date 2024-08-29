package thread.executor;

import thread.executor.util.ExecutorUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PrestartPoolMain {
    /*
    스레드 미리 생성하기
        응답시간이 아주 중요한 서버라면 서버가 고객의 처음 요청을 받기 전에 스레드를 스레드 풀에 미리 생성해두고 싶을 수 있다.
        스레드를 미리 생성해두면 처음 요청에서 사용되는 스레드의 생성 시간을 줄일 수 있다.
        ThreadPoolExecutor.prestartAllCoreThreads() 를 사용하면 기본 스레드를 미리 생성할 수 있다.
        참고로 ExecutorService 는 이 메서드를 제공하지 않는다.
    */
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1000);
        ExecutorUtils.printState(es);

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) es;
        poolExecutor.prestartAllCoreThreads();

        ExecutorUtils.printState(es);
    }
}
