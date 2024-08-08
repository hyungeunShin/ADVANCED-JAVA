package thread.sync.sample;

public interface BankAccount {
    boolean withdraw(int amount);

    int getBalance();
}
