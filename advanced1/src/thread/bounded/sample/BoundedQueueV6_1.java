package thread.bounded.sample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//BlockingQueue 대기
public class BoundedQueueV6_1 implements BoundedQueue {
    private final BlockingQueue<String> queue;

    public BoundedQueueV6_1(int max) {
        queue = new ArrayBlockingQueue<>(max);
    }

    /*
    BlockingQueue
        자바는 생산자 소비자 문제를 해결하기 위해 java.util.concurrent.BlockingQueue 라는 특별한 멀티스레드 자료 구조를 제공한다.
        큐를 상속 받았기 때문에 추가로 큐의 기능들도 사용할 수 있다.

        BlockingQueue 는 인터페이스이고 다음과 대표적인 구현체를 제공한다.
            - ArrayBlockingQueue : 배열 기반으로 구현되어 있고 버퍼의 크기가 고정되어 있다.
            - LinkedBlockingQueue : 링크 기반으로 구현되어 있고 버퍼의 크기를 고정할 수도 또는 무한하게 사용할 수도 있다.

        이것은 이름 그대로 스레드를 차단(Blocking) 할 수 있는 큐다.
            - 데이터 추가 차단: 큐가 가득 차면 데이터 추가 작업을 시도하는 스레드는 공간이 생길 때까지 차단된다.
            - 데이터 획득 차단: 큐가 비어 있으면 획득 작업을 시도하는 스레드는 큐에 데이터가 들어올 때까지 차단된다.

        큐가 가득 찼을 때 생각할 수 있는 선택지는 4가지가 있다.
            1. 예외를 던진다. 예외를 받아서 처리한다.
            2. 대기하지 않는다. 즉시 false 를 반환한다.
            3. 대기한다.
            4. 특정 시간 만큼만 대기한다.

            이런 문제를 해결하기 위해 BlockingQueue 는 각 상황에 맞는 다양한 메서드를 제공한다.
                1. 대기 시 예외
                    add(e): 지정된 요소를 큐에 추가하며, 큐가 가득 차면 IllegalStateException 예외를 던진다.
                    remove(): 큐에서 요소를 제거하며 반환한다. 큐가 비어 있으면 NoSuchElementException 예외를 던진다.
                    element(): 큐의 머리 요소를 반환하지만, 요소를 큐에서 제거하지 않는다. 큐가 비어 있으면 NoSuchElementException 예외를 던진다.

                2. 대기 시 즉시 반환
                    offer(e): 지정된 요소를 큐에 추가하려고 시도하며, 큐가 가득 차면 false 를 반환한다.
                    poll(): 큐에서 요소를 제거하고 반환한다. 큐가 비어 있으면 null 을 반환한다.
                    peek(): 큐의 머리 요소를 반환하지만, 요소를 큐에서 제거하지 않는다. 큐가 비어 있으면 null 을 반환한다.

                3. 대기
                    put(e): 지정된 요소를 큐에 추가할 때까지 대기한다. 큐가 가득 차면 공간이 생길 때까지 대기한다.
                    take(): 큐에서 요소를 제거하고 반환한다. 큐가 비어 있으면 요소가 준비될 때까지 대기한다.
                    Examine (관찰): 해당 사항 없음.

                4. 특정 시간 대기
                    offer(e, time, unit): 지정된 요소를 큐에 추가하려고 시도하며, 지정된 시간 동안 큐가 비워지기를 기다리다가
                    시간이 초과되면 false 를 반환한다.
                    poll(time, unit): 큐에서 요소를 제거하고 반환한다. 큐에 요소가 없다면 지정된 시간 동안 요소가 준비되기를
                    기다리다가 시간이 초과되면 null 을 반환한다.
                    Examine (관찰): 해당 사항 없음.

    ArrayBlockingQueue 는 내부에서 ReentrantLock 을 사용한다.
    그리고 생산자 전용 대기실과 소비자 전용 대기실이 있다.
    만약 버퍼가 가득 차면 생산자 스레드는 생산자 전용 대기실에서 대기(await())한다.
    생산자 스레드가 생산을 완료하면 소비자 전용 대기실에 signal() 로 신호를 전달한다.
    BoundedQueueV5 기능과 차이가 있다면 인터럽트가 걸릴 수 있도록 lock.lock() 대신에 lock.lockInterruptibly() 을 사용한 점과 내부 자료 구조의 차이 정도이다.
    참고로 lock.lock() 은 인터럽트를 무시한다.
    */
    @Override
    public void put(String data) {
        //BoundedQueueV5.put() 과 같은 기능을 제공한다.
        try {
            queue.put(data);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String take() {
        //BoundedQueueV5.take() 와 같은 기능을 제공한다.
        try {
            return queue.take();
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
