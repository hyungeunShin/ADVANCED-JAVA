package thread.cas.increment;

public class IncrementPerformanceMain {
    public static final int THREAD_COUNT = 100_000_000;

    /*
    BasicInteger
        - 가장 빠르다.
        - CPU 캐시를 적극 사용한다. CPU 캐시의 위력을 알 수 있다.
        - 안전한 임계 영역도 없고 volatile 도 사용하지 않기 때문에 멀티스레드 상황에는 사용할 수 없다.
        - 단일 스레드가 사용하는 경우에 효율적이다.
    VolatileInteger
        - volatile 을 사용해서 CPU 캐시를 사용하지 않고 메인 메모리를 사용한다.
        - 안전한 임계 영역이 없기 때문에 멀티스레드 상황에는 사용할 수 없다.
        - 단일 스레드가 사용하기에는 BasicInteger 보다 느리다. 그리고 멀티스레드 상황에도 안전하지 않다.
    SyncInteger
        - synchronized 를 사용한 안전한 임계 영역이 있기 때문에 멀티스레드 상황에도 안전하게 사용할 수 있다.
        - MyAtomicInteger 보다 성능이 느리다.
    MyAtomicInteger
        - 자바가 제공하는 AtomicInteger 를 사용한다. 멀티스레드 상황에 안전하게 사용할 수 있다.
        - 성능도 synchronized, Lock(ReentrantLock) 을 사용하는 경우보다 2배 정도 빠르다.
        - AtomicInteger 가 제공하는 incrementAndGet() 메서드는 락을 사용하지 않고 원자적 연산을 만들어낸다.
    */
    public static void main(String[] args) throws InterruptedException {
        test(new IncrementIntegerV1());
        test(new IncrementIntegerV2());
        test(new IncrementIntegerV3());
        test(new IncrementIntegerV4());
    }

    private static void test(IncrementInteger incrementInteger) throws InterruptedException {
        long s = System.currentTimeMillis();
        for(int i = 0; i < THREAD_COUNT; i++) {
            incrementInteger.increment();
        }
        long e = System.currentTimeMillis();

        System.out.println(incrementInteger.getClass().getSimpleName() + ".ms : " + (e - s));
    }
}
