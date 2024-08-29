package thread.cas.increment;

public class IncrementIntegerV1 implements IncrementInteger {
    private int value;

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
