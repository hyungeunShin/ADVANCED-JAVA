package thread.sync.sample;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV3 implements BankAccount {
    private int balance;

    public BankAccountV3(int balance) {
        this.balance = balance;
    }

    /*
    synchronized 의 가장 큰 장점이자 단점은 한 번에 하나의 스레드만 실행할 수 있다는 점이다.
    여러 스레드가 동시에 실행하지 못하기 때문에 전체로 보면 성능이 떨어질 수 있다.
    따라서 synchronized 를 통해 여러 스레드를 동시에 실행할 수 없는 코드 구간은 꼭 필요한 곳으로 한정해서 설정해야 한다.

    synchronized(this) : 여기서 괄호 () 안에 들어가는 값은 락을 획득할 인스턴스의 참조이다.
    */
    @Override
    public boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        synchronized(this) {
            log("[검증 시작] 출금액 : " + amount + ", 잔액 : " + balance);
            if(balance < amount) {
                log("[검증 실패] 출금액 : " + amount + ", 잔액 : " + balance);
                return false;
            }
            log("[검증 완료] 출금액 : " + amount + ", 잔액 : " + balance);

            sleep(1000);

            balance -= amount;
            log("[출금 완료] 출금액 : " + amount + ", 잔액 : " + balance);
        }

        log("거래 종료");

        return true;
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }
}
