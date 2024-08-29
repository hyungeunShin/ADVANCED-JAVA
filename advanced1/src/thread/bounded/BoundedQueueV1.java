package thread.bounded;

import util.MyLogger;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedQueueV1 implements BoundedQueue {
    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV1(int max) {
        this.max = max;
    }

    /*
    p : producer, c : consumer

    생산자 스레드가 먼저 실행되는 경우 p3가 보관하는 data3은 버려지고 c3는 데이터를 받지 못한다.(null 을 받는다.)
    소비자 스레드가 먼저 실행되는 경우 c1, c2, c3 는 데이터를 받지 못한다.(null 을 받는다.) 그리고 p3가 보관하는 data3은 버려진다.

    버퍼가 가득 찬 경우: 생산자 입장에서 버퍼에 여유가 생길 때 까지 조금만 기다리면 되는데 기다리지 못하고 데이터를 버리는 것은 아쉽다.
    버퍼가 빈 경우: 소비자 입장에서 버퍼에 데이터가 채워질 때 까지 조금만 기다리면 되는데 기다리지 못하고 null 데이터를 얻는 것은 아쉽다.
    문제의 해결 방안은 단순하다. 스레드가 기다리면 되는 것이다.
    */
    @Override
    public synchronized void put(String data) {
        if(queue.size() == max) {
            MyLogger.log("[put] 큐가 가득 참, 데이터 버림 : " + data);
            return;
        }
        queue.offer(data);
    }

    @Override
    public synchronized String take() {
        if(queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    /*
    원칙적으로 toString() 에도 synchronized 를 적용해야 한다.
    그래야 toString() 을 통한 조회 시점에도 정확한 데이터를 조회할 수 있다.
    하지만 예제 코드를 단순하게 유지하기 위해 여기서는 toString() 에 synchronized 를 사용하지 않겠다.
    */
    @Override
    public String toString() {
        return queue.toString();
    }
}
