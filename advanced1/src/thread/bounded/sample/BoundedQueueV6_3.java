package thread.bounded.sample;

import util.MyLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//BlockingQueue 특정 시간 대기
public class BoundedQueueV6_3 implements BoundedQueue {
    private final BlockingQueue<String> queue;

    public BoundedQueueV6_3(int max) {
        queue = new ArrayBlockingQueue<>(max);
    }

    @Override
    public void put(String data) {
        try {
            //성공하면 true 를 반환한다.
            //버퍼가 가득 차서 스레드가 대기해야 하는 상황이면 지정한 시간까지 대기한다. 대기 시간이 지나면 false 를 반환
            boolean result = queue.offer(data, 1, TimeUnit.NANOSECONDS);
            MyLogger.log("저장 시도 결과 : " + result);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String take() {
        try {
            //버퍼에 데이터가 없어서 스레드가 대기해야 하는 상황이면 지정한 시간까지 대기한다.
            //대기 시간을 지나면 null 을 반환
            return queue.poll(2, TimeUnit.SECONDS);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
