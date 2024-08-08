package thread.cas.sample;

public class IncrementIntegerV2 implements IncrementInteger {
    private volatile int value;

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
