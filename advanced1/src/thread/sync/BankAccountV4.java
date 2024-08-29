package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV4 implements BankAccount {
    private int balance;
    private final Lock lock;

    public BankAccountV4(int balance) {
        this.balance = balance;
        lock = new ReentrantLock();
    }

    /*
    t1 이 먼저 실행된다고 가정하겠다.
    t1 : ReentrantLock 에 있는 락을 획득한다.
        락을 획득하는 경우 RUNNABLE 상태가 유지되고 임계 영역의 코드를 실행할 수 있다.
    t1 : 임계 영역의 코드를 실행한다.
    t2 : ReentrantLock 에 있는 락의 획득을 시도한다. 하지만 락이 없다.
    t2 : 락을 획득하지 못하면 WAITING 상태가 되고 대기 큐에서 관리된다.
        ReentrantLock 내부에는 락과 락을 얻지 못해 대기하는 스레드를 관리하는 대기 큐가 존재한다.
        LockSupport.park() 가 내부에서 호출된다.
        참고로 tryLock(long time, TimeUnit unit) 와 같은 시간 대기 기능을 사용하면 TIMED_WAITING 이 되고 대기 큐에서 관리된다.
    t1 : 임계 영역의 수행을 완료했다.
    t1 : 임계 영역을 수행하고 나면 lock.unlock() 을 호출한다.
    t1 : 락을 반납한다.
    t1 : 대기 큐의 스레드를 하나 깨운다. LockSupport.unpark(thread) 가 내부에서 호출된다.

    t2 : RUNNABLE 상태가 되면서 깨어난 스레드는 락 획득을 시도한다.
        이때 락을 획득하면 lock.lock() 을 빠져나오면서 대기 큐에서도 제거된다.
        이때 락을 획득하지 못하면 다시 대기 상태가 되면서 대기 큐에 유지된다.
    t2 : 락을 획득한 t2 스레드는 RUNNABLE 상태로 임계 영역을 수행한다.
    t2 : 잔액이 출금액보다 적으므로 검증 로직을 통과하지 못한다.
    t2 : lock.unlock() 을 호출해서 락을 반납하고 대기 큐의 스레드를 하나 깨우려고 시도한다. 대기 큐에 스레드가 없으므로 이때는 깨우지 않는다.
    */
    @Override
    public boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        lock.lock();
        try {
            log("[검증 시작] 출금액 : " + amount + ", 잔액 : " + balance);
            if(balance < amount) {
                log("[검증 실패] 출금액 : " + amount + ", 잔액 : " + balance);
                return false;
            }
            log("[검증 완료] 출금액 : " + amount + ", 잔액 : " + balance);

            sleep(1000);

            balance -= amount;
            log("[출금 완료] 출금액 : " + amount + ", 잔액 : " + balance);
        } finally {
            /*
            임계 영역이 끝나면 반드시 락을 반납해야 한다. 그렇지 않으면 대기하는 스레드가 락을 얻지 못한다.
            따라서 lock.unlock() 은 반드시 finally 블럭에 작성해야한다.
            */
            lock.unlock();
        }

        log("거래 종료");

        return true;
    }

    @Override
    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
