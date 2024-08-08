package thread.bounded.sample;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV4 implements BoundedQueue {
    //synchronized 에서 사용하는 객체 내부에 있는 모니터 락이 아니라 ReentrantLock 락
    private final Lock lock = new ReentrantLock();

    //synchronized 에서 사용하는 스레드 대기 공간이 아니라 ReentrantLock 을 사용하여 만든 스레드 대기 공간
    private final Condition condition = lock.newCondition();

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV4(int max) {
        this.max = max;
    }

    /*
    생산자가 생산자를 깨우고 소비자가 소비자를 깨우는 비효율 문제를 어떻게 해결할 수 있을까?
        핵심은 생산자 스레드는 데이터를 생성하고 대기중인 소비자 스레드에게 알려주어야 한다.
        반대로 소비자 스레드는 데이터를 소비하고 대기중인 생산자 스레드에게 알려주면 된다.
        결국 생산자 스레드가 대기하는 대기 집합과 소비자 스레드가 대기하는 대기 집합을 둘로 나누면 된다.
        그리고 생산자 스레드가 데이터를 생산하면 소비자 스레드가 대기하는 대기 집합에만 알려주고 소비자 스레드가 데이터를 소비하면 생산자 스레드가 대기하는 대기 집합에만 열려주면 되는 것이다.
        이렇게 생산자용, 소비자용 대기 집합을 서로 나누어 분리하면 비효율 문제를 깔끔하게 해결할 수 있다.
        Lock, ReentrantLock 을 사용하여 대기 집합을 분리하면 된다.
        
    Condition
        Condition condition = lock.newCondition()
            Condition 은 ReentrantLock 을 사용하는 스레드가 대기하는 스레드 대기 공간이다.
            lock.newCondition() 메서드를 호출하면 스레드 대기 공간이 만들어진다.
            Lock(ReentrantLock) 의 스레드 대기 공간은 이렇게 만들 수 있다.
            참고로 Object.wait() 에서 사용한 스레드 대기 공간은 모든 객체 인스턴스가 내부에 기본으로 가지고 있다.
            반면에 Lock(ReentrantLock) 을 사용하는 경우 이렇게 스레드 대기 공간을 직접 만들어서 사용해야 한다.

        condition.await()
            Object.wait() 와 유사한 기능이다. 지정한 condition 에 현재 스레드를 대기(WAITING) 상태로 보관한다.
            이때 ReentrantLock 에서 획득한 락을 반납하고 대기 상태로 condition 에 보관된다.

        condition.signal()
            Object.notify() 와 유사한 기능이다. 지정한 condition 에서 대기중인 스레드를 하나 깨운다.
            깨어난 스레드는 condition 에서 빠져나온다.

    실행 결과는 BoundedQueueV3 와 같다. 아직 condition(스레드 대기 공간)을 분리하지 않았기 때문에 기존과 같다.
    다만 구현을 synchronized 로 했는가 아니면 ReentrantLock 을 사용해서 했는가에 차이가 있을 뿐이다.
    */
    @Override
    public void put(String data) {
        lock.lock();
        try {
            while(queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                condition.await();
                log("[put] 생산자 깨어남");
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, signal() 호출");
            condition.signal();
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String take() {
        lock.lock();
        try {
            while(queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                condition.await();
                log("[take] 소비자 깨어남");
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, signal() 호출");
            condition.signal();
            return data;
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
