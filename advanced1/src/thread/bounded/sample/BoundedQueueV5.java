package thread.bounded.sample;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV5 implements BoundedQueue {
    private final Lock lock = new ReentrantLock();

    private final Condition producerCondition = lock.newCondition();
    private final Condition consumerCondition = lock.newCondition();

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV5(int max) {
        this.max = max;
    }

    /*
    Object.notify() vs Condition.signal()
        Object.notify()
            - 대기 중인 스레드 중 임의의 하나를 선택해서 깨운다.
              스레드가 깨어나는 순서는 정의되어 있지 않으며 JVM 구현에 따라 다르다.
              보통은 먼저 들어온 스레드가 먼저 수행되지만 구현에 따라 다를 수 있다.
            - synchronized 블록 내에서 모니터 락을 가지고 있는 스레드가 호출해야 한다.

        Condition.signal()
            - 대기 중인 스레드 중 하나를 깨우며 일반적으로는 FIFO 순서로 깨운다.
              이 부분은 자바 버전과 구현에 따라 달라질 수 있지만 보통 Condition 의 구현은 Queue 구조를 사용하기 때문에 FIFO 순서로 깨운다.
            - ReentrantLock 을 가지고 있는 스레드가 호출해야 한다.

    스레드의 대기
        synchronized 대기
            - 대기1: 락 획득 대기
                - BLOCKED 상태로 락 획득 대기
                - synchronized 를 시작할 때 락이 없으면 대기
                - 다른 스레드가 synchronized 를 빠져나갈 때 대기가 풀리며 락 획득 시도
            - 대기2: wait() 대기
                - WAITING 상대로 대기
                - wait() 를 호출 했을 때 스레드 대기 집합에서 대기
                - 다른 스레드가 notify() 를 호출 했을 때 빠져나감

            락 대기 집합
                소비자 스레드 c1, c2, c3 와 생산자 스레드 p1, p2, p3가 있다고 가정한다.
                소비자 스레드 c1이 가장 먼저 락을 획득한다.
                c2, c3는 락 획득을 시도하지만 모니터 락이 없기 때문에 락을 대기하며 BLOCKED 상태가 된다.
                c1은 나중에 락을 반납할 것이다. 그러면 c2, c3 중에 하나가 락을 획득해야 한다.
                그런데 잘 생각해보면 락을 기다리는 c2, c3 도 어딘가에서 관리가 되어야 한다.
                그래야 락이 반환되었을 때 자바가 c2, c3 중에 하나를 선택해서 락을 제공할 수 있다.
                예를 들어서 List, Set, Queue 같은 자료구조에 관리가 되어야 한다.

                락 대기 집합은 락을 기다리는 BLOCKED 상태의 스레드들을 관리한다.
                락 대기 집합은 자바 내부에 구현되어 있기 때문에 모니터 락과 같이 개발자가 확인하기는 어렵다.

                c1은 큐에 획득할 데이터가 없기 때문에 락을 반납하고 WAITING 상태로 스레드 대기 집합에서 대기한다.
                이후에 락 대기 집합에 있는 c2가 락을 획득하고 임계 영역을 수행한다.
                큐에 획득할 데이터가 없기 때문에 락을 반납하고 WAITING 상태로 스레드 대기 집합에서 대기한다.
                c3도 동일한 로직을 수행한다.

                p1이 락을 획득하고 데이터를 저장한 다음 스레드 대기 집합에 이 사실을 알린다.
                스레드 대기 집합에 있는 c1이 스레드 대기 집합을 빠져나간다.
                하지만 아직 끝난 것이 아니다. 락을 얻어서 락 대기 집합까지 빠져나가야 임계 영역을 수행할 수 있다.
                c1은 락 획득을 시도하지만 락이 없다. 따라서 락 대기 집합에서 관리된다.
                c1은 락 획득을 기다리며 BLOCKED 상태로 락 대기 집합에서 기다린다.
                드디어 p1이 락을 반납한다.
                락이 반납되면 락 대기 집합에 있는 스레드 중 하나가 락을 획득한다. 여기서는 c1이 락을 획득한다.
                c1은 드디어 락 대기 집합까지 탈출하고 임계 영역을 수행한다.

                개념상 락 대기 집합이 1차 대기소이고 스레드 대기 집합이 2차 대기소이다.
                2차 대기소에 있는 스레드는 2차 대기소를 빠져나온다고 끝이 아니다.
                1차 대기소까지 빠져나와야 임계 영역에서 로직을 수행할 수 있다.

            ※ 정리
                자바의 모든 객체 인스턴스는 멀티스레드와 임계 영역을 다루기 위해 내부에 3가지 기본 요소를 가진다.
                    - 모니터 락
                    - 락 대기 집합(모니터 락 대기 집합)
                    - 스레드 대기 집합

                여기서 락 대기 집합이 1차 대기소이고 스레드 대기 집합이 2차 대기소라 생각하면 된다.
                2차 대기소에 들어간 스레드는 2차, 1차 대기소를 모두 빠져나와야 임계 영역을 수행할 수 있다.

                이 3가지 요소는 서로 맞물려 돌아간다.
                    - synchronized 를 사용한 임계 영역에 들어가려면 모니터 락이 필요하다.
                    - 모니터 락이 없으면 락 대기 집합에 들어가서 BLOCKED 상태로 락을 기다린다.
                    - 모니터 락을 반납하면 락 대기 잡합에 있는 스레드 중 하나가 락을 획득하고 BLOCKED -> RUNNABLE 상태가 된다.
                    - wait() 를 호출해서 스레드 대기 집합에 들어가기 위해서는 모니터 락이 필요하다.
                    - 스레드 대기 집합에 들어가면 모니터 락을 반납한다.
                    - 스레드가 notify() 를 호출하면 스레드 대기 집합에 있는 스레드 중 하나가 스레드 대기 집합을 빠져나온다. 그리고 모니터 락 획득을 시도한다.
                        - 모니터 락을 획득하면 임계 영역을 수행한다.
                        - 모니터 락을 획득하지 못하면 락 대기 집합에 들어가서 BLOCKED 상태로 락을 기다린다.

        ReentrantLock 대기
            - 대기1: ReentrantLock 락 획득 대기
                - ReentrantLock 의 대기 큐에서 관리
                - WAITING 상태로 락 획득 대기
                - lock.lock() 을 호출 했을 때 락이 없으면 대기
                - 다른 스레드가 lock.unlock() 을 호출 했을 때 대기가 풀리며 락 획득 시도, 락을 획득하면 대기 큐를 빠져나감
            - 대기2: await() 대기
                - condition.await() 를 호출 했을 때 condition 객체의 스레드 대기 공간에서 관리
                - WAITING 상대로 대기
                - 다른 스레드가 condition.signal() 을 호출 했을 때 condition 객체의 스레드 대기 공간에서 빠져나감

            락 대기 집합소
                synchronized 와 마찬가지로 ReentrantLock 도 대기소가 2단계로 되어 있다.
                2단계 대기소인 condition 객체의 스레드 대기 공간을 빠져나온다고 바로 실행되는 것이 아니다.
                임계 영역 안에서는 항상 락이 있는 하나의 스레드만 실행될 수 있다.
                여기서는 ReentrantLock 의 락을 획득해야 RUNNABLE 상태가 되면서 그 다음 코드를 실행할 수 있다.
                락을 획득하지 못하면 WAITING 상태로 락을 획득할 때 까지 ReentrantLock 의 대기 큐에서 대기한다.
    */
    @Override
    public void put(String data) {
        lock.lock();
        try {
            while(queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                producerCondition.await();
                log("[put] 생산자 깨어남");
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, consumerCondition.signal() 호출");
            consumerCondition.signal();
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
                consumerCondition.await();
                log("[take] 소비자 깨어남");
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, producerCondition.signal() 호출");
            producerCondition.signal();
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
