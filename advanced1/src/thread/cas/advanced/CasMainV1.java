package thread.cas.advanced;

import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV1 {
    /*
    자바는 AtomicXxx 의 compareAndSet() 메서드를 통해 CAS 연산을 지원한다.
        compareAndSet(0, 1)
            - atomicInteger 가 가지고 있는 값이 현재 0이면 이 값을 1로 변경하라는 매우 단순한 메서드이다.
            - 만약 atomicInteger 의 값이 현재 0이라면 atomicInteger 의 값은 1로 변경된다. 이 경우 true 를 반환한다.
            - 만약 atomicInteger 의 값이 현재 0이 아니라면 atomicInteger 의 값은 변경되지 않는다. 이 경우 false 를 반환한다.
            - 여기서 가장 중요한 점은 이 메서드는 원자적으로 실행된다는 점이다.

    생각해보면 compareAndSet(0, 1) 는 2개로 나누어진 명령어이다. 따라서 원자적이지 않은 연산처럼 보인다.
        1. 먼저 메인 메모리에 있는 값을 확인한다.
        2. 해당 값이 기대하는 값(0)이라면 원하는 값(1)으로 변경한다.
    CAS 연산은 이렇게 원자적이지 않은 두 개의 연산을 CPU 하드웨어 차원에서 특별하게 하나의 원자적인 연산으로 묶어서 제공하는 기능이다.
    이것은 소프트웨어가 제공하는 기능이 아니라 하드웨어가 제공하는 기능이다.
    대부분의 현대 CPU 들은 CAS 연산을 위한 명령어를 제공한다.

    CPU 는 다음 두 과정을 묶어서 하나의 원자적인 명령으로 만들어버린다. 따라서 중간에 다른 스레드가 개입할 수 없다.
        1. 인스턴스(x001)의 값을 확인한다.
        2. 읽은 값이 0이면 1로 변경한다.
    CPU 는 두 과정을 하나의 원자적인 명령으로 만들기 위해 1번과 2번 사이에 다른 스레드가 x001 의 값을 변경하지 못하게 막는다.
    참고로 1번과 2번 사이의 시간은 CPU 입장에서 보면 아주 잠깐 찰나의 순간이다.
    그래서 성능에 큰 영향을 끼치지 않는다.

    그런데 이 기능이 어떻게 락을 일부 대체할 수 있는 것일까?
    */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println("start value : " + atomicInteger.get());

        boolean b1 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result1 : " + b1 + ", value : " + atomicInteger.get());

        boolean b2 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result2 : " + b2 + ", value : " + atomicInteger.get());
    }
}
