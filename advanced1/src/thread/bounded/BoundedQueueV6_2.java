package thread.bounded;

import util.MyLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//BlockingQueue 즉시 반환
public class BoundedQueueV6_2 implements BoundedQueue {
    private final BlockingQueue<String> queue;

    public BoundedQueueV6_2(int max) {
        queue = new ArrayBlockingQueue<>(max);
    }

    @Override
    public void put(String data) {
        //성공하면 true 를 반환하고 버퍼가 가득 차면 즉시 false 를 반환
        boolean result = queue.offer(data);
        MyLogger.log("저장 시도 결과 : " + result);
    }

    @Override
    public String take() {
        //poll() 버퍼에 데이터가 없으면 즉시 null 을 반환
        return queue.poll();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
