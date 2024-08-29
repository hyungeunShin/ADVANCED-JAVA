package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV2 implements BankAccount {
    private int balance;

    public BankAccountV2(int balance) {
        this.balance = balance;
    }

    /*
    t1 이 먼저 실행된다고 가정하겠다.
    스레드 t1 이 먼저 synchronized 키워드가 있는 withdraw() 메서드를 호출한다.
    synchronized 메서드를 호출하려면 먼저 해당 인스턴스의 락이 필요하다.
    락이 있으므로 스레드 t1 은 BankAccount(x001) 인스턴스에 있는 락을 획득한다.
    스레드 t1 은 해당 인스턴스의 락을 획득했기 때문에 withdraw() 메서드에 진입할 수 있다.

    스레드 t2 도 withdraw() 메서드 호출을 시도한다.
    synchronized 메서드를 호출하려면 먼저 해당 인스턴스의 락이 필요하다.
    스레드 t2 는 BankAccount(x001) 인스턴스에 있는 락 획득을 시도한다. 하지만 락이 없다.
    이렇게 락이 없으면 t2 스레드는 락을 획득할 때 까지 BLOCKED 상태로 대기한다.
    t2 스레드의 상태는 RUNNABLE -> BLOCKED 상태로 변하고 락을 획득할 때 까지 무한정 대기한다.

    t1 : 출금을 위한 검증 로직을 수행한다. 조건을 만족하므로 검증 로직을 통과한다.
    t1 : 잔액 1000원에서 800원을 출금하고 계산 결과인 200원을 잔액(balance)에 반영한다.
    t1 : 메서드 호출이 끝나면 락을 반납한다.
    t2 : 인스턴스에 락이 반납되면 락 획득을 대기하는 스레드는 자동으로 락을 획득한다.
    이때 락을 획득한 스레드는 BLOCKED -> RUNNABLE 상태가 되고 다시 코드를 실행한다.
    스레드 t2 는 해당 인스턴스의 락을 획득했기 때문에 withdraw() 메서드에 진입할 수 있다.
    t2 : 출금을 위한 검증 로직을 수행한다. 조건을 만족하지 않으므로 false 를 반환한다.

    참고로 BLOCKED 상태가 되면 락을 다시 획득하기 전까지는 계속 대기하고 CPU 실행 스케줄링에 들어가지 않는다.

    참고: 락을 획득하는 순서는 보장되지 않는다.
        만약 BankAccount(x001) 인스턴스의 withdraw() 를 수 많은 스레드가 동시에 호출한다면 1개의 스레드만 락을 획득하고 나머지는 모두 BLOCKED 상태가 된다.
        그리고 이후에 BankAccount(x001) 인스턴스에 락을 반납하면 해당 인스턴스의 락을 기다리는 수 많은 스레드 중에 하나의 스레드만 락을 획득하고 락을 획득한 스레드만 BLOCKED -> RUNNABLE 상태가 된다.
        이때 어떤 순서로 락을 획득하는지는 자바 표준에 정의되어 있지 않다.
        따라서 순서를 보장하지 않고 환경에 따라서 순서가 달라질 수 있다.
    */
    @Override
    public synchronized boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        log("[검증 시작] 출금액 : " + amount + ", 잔액 : " + balance);
        if(balance < amount) {
            log("[검증 실패] 출금액 : " + amount + ", 잔액 : " + balance);
            return false;
        }
        log("[검증 완료] 출금액 : " + amount + ", 잔액 : " + balance);

        sleep(1000);

        balance -= amount;
        log("[출금 완료] 출금액 : " + amount + ", 잔액 : " + balance);

        log("거래 종료");

        return true;
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }
}
