package thread.cas.sample;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementIntegerV4 implements IncrementInteger {
    /*
    자바는 SyncInteger 와 같이 멀티스레드 상황에서 안전하게 증가 연산을 수행할 수 있는 AtomicInteger 라는 클래스를 제공한다.
    이름 그대로 원자적인 Integer 라는 뜻이다.
    참고: AtomicInteger, AtomicLong, AtomicBoolean 등 다양한 AtomicXxx 클래스가 존재한다.
    */

    //초기값을 지정한다. 생략하면 0 부터 시작
    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void increment() {
        //값을 하나 증가하고 증가된 결과를 반환
        atomicInteger.incrementAndGet();
    }

    @Override
    public int get() {
        return atomicInteger.get();
    }
}
