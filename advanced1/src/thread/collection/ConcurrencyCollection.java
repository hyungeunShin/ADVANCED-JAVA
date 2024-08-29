package thread.collection;

public class ConcurrencyCollection {
    /*
    여러 스레드가 동시에 접근해도 괜찮은 경우를 스레드 세이프(Thread Safe)하다고 한다.
    java.util 패키지에 소속되어 있는 컬렉션 프레임워크의 대부분은 스레드 세이프 하지 않다.
    우리가 일반적으로 자주 사용하는 ArrayList, LinkedList, HashSet, HashMap 등 수 많은 자료 구조들은 단순한 연산을 제공하는 것 처럼 보인다.
    예를 들어서 데이터를 추가하는 add() 와 같은 연산은 마치 원자적인 연산처럼 느껴진다.
    하지만 그 내부에서는 수 많은 연산들이 함께 사용된다.
    배열에 데이터를 추가하고 사이즈를 변경하고 배열을 새로 만들어서 배열의 크기도 늘리고 노드를 만들어서 링크에 연결하는 등 수 많은 복잡한 연산이 함께 사용된다.
    따라서 일반적인 컬렉션들은 절대로 스레드 세이프 하지 않다. 또한 컬렉션 프레임워크가 제공하는 대부분의 연산은 원자적인 연산이 아니다.

    단일 스레드가 컬렉션에 접근하는 경우라면 아무런 문제가 없지만 멀티스레드 상황에서 여러 스레드가 동시에 컬렉션에 접근하는 경우라면 java.util 패키지가 제공하는 일반적인 컬렉션들은 사용하면 안된다.
    물론 일부 예외도 있다.
    최악의 경우 실무에서 두 명의 사용자가 동시에 컬렉션에 데이터를 보관했는데 한명의 사용자 데이터가 사라질 수 있다.

    그렇다면 처음부터 모든 자료 구조에 synchronized 를 사용해서 동기화를 해두면 어떨까?
    synchronized, Lock, CAS 등 모든 방식은 정도의 차이는 있지만 성능과 트레이드 오프가 있다.
    결국 동기화를 사용하지 않는 것이 가장 빠르다.
    그리고 컬렉션이 항상 멀티스레드에서 사용되는 것도 아니다.
    미리 동기화를 해둔다면 단일 스레드에서 사용할 때 동기화로 인해 성능이 저하된다.
    따라서 동기화의 필요성을 정확히 판단하고 꼭 필요한 경우에만 동기화를 적용하는 것이 필요하다.

    [SynchronizedListMain.class]
        Collections 는 다음과 같이 다양한 synchronized 동기화 메서드를 지원한다.
        이 메서드를 사용하면 List, Collection, Map, Set 등 다양한 동기화 프록시를 만들어낼 수 있다.
            - synchronizedList()
            - synchronizedCollection()
            - synchronizedMap()
            - synchronizedSet()
            - synchronizedNavigableMap()
            - synchronizedNavigableSet()
            - synchronizedSortedMap()
            - synchronizedSortedSet()

        Collections 가 제공하는 동기화 프록시 기능 덕분에 스레드 안전하지 않은 수 많은 컬렉션들을 매우 편리하게 스레드 안전한 컬렉션으로 변경해서 사용할 수 있다.

        하지만 synchronized 프록시를 사용하는 방식은 다음과 같은 단점이 있다.
        첫째 동기화 오버헤드가 발생한다.
        비록 synchronized 키워드가 멀티스레드 환경에서 안전한 접근을 보장하지만 각 메서드 호출 시마다 동기화 비용이 추가된다. 이로 인해 성능 저하가 발생할 수 있다.
        둘째 전체 컬렉션에 대해 동기화가 이루어지기 때문에 잠금 범위가 넓어질 수 있다.
        이는 잠금 경합(lock contention)을 증가시키고 병렬 처리의 효율성을 저하시키는 요인이 된다.
        모든 메서드에 대해 동기화를 적용하다 보면 특정 스레드가 컬렉션을 사용하고 있을 때 다른 스레드들이 대기해야 하는 상황이 빈번해질 수 있다.
        셋째 정교한 동기화가 불가능하다.
        synchronized 프록시를 사용하면 컬렉션 전체에 대한 동기화가 이루어지지만 특정 부분이나 메서드에 대해 선택적으로 동기화를 적용하는 것은 어렵다.
        이는 과도한 동기화로 이어질 수 있다.
        쉽게 이야기해서 이 방식은 단순 무식하게 모든 메서드에 synchronized 를 걸어버리는 것이다.
        따라서 동기화에 대한 최적화가 이루어지지 않는다.

        자바는 이런 단점을 보완하기 위해 java.util.concurrent 패키지에 동시성 컬렉션(concurrent collection)을 제공한다.

    [SynchronizedMain.class]
        자바 1.5부터 동시성을 위한 컬렉션이 있다. 동시성 컬렉션은 스레드 안전한 컬렉션을 뜻한다.
        java.util.concurrent 패키지에는 고성능 멀티스레드 환경을 지원하는 다양한 동시성 컬렉션 클래스들을 제공한다.
        예를 들어 ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue 등이 있다.
        이 컬렉션들은 더 정교한 잠금 메커니즘을 사용하여 동시 접근을 효율적으로 처리하며 필요한 경우 일부 메서드에 대해서만 동기화를 적용하는 등 유연한 동기화 전략을 제공한다.
        여기에 다양한 성능 최적화 기법들이 적용되어 있는데 synchronized, Lock(ReentrantLock), CAS, 분할 잠금 기술(segment lock)등 다양한 방법을 섞어서 매우 정교한 동기화를 구현하면서 동시에 성능도 최적화했다.

        동시성 컬렉션의 종류
            List
                - CopyOnWriteArrayList : ArrayList 의 대안
            Set
                - CopyOnWriteArraySet : HashSet 의 대안
                - ConcurrentSkipListSet : TreeSet 의 대안(정렬된 순서 유지, Comparator 사용 가능)
            Map
                - ConcurrentHashMap : HashMap 의 대안
                - ConcurrentSkipListMap : TreeMap 의 대안(정렬된 순서 유지, Comparator 사용 가능)
            Queue
                - ConcurrentLinkedQueue : 동시성 큐, 비 차단(non-blocking) 큐이다.
            Deque
                - ConcurrentLinkedDeque : 동시성 데크, 비 차단(non-blocking) 큐이다.

            참고로 LinkedHashSet, LinkedHashMap 처럼 입력 순서를 유지하는 동시에 멀티스레드 환경에서 사용할 수 있는 Set, Map 구현체는 제공하지 않는다.
            필요하다면 Collections.synchronizedXxx() 를 사용해야 한다.

        스레드를 차단하는 블로킹 큐
            - ArrayBlockingQueue
                크기가 고정된 블로킹 큐
                공정(fair) 모드를 사용할 수 있다. 공정(fair) 모드를 사용하면 성능이 저하될 수 있다.
            - LinkedBlockingQueue
                크기가 무한하거나 고정된 블로킹 큐
            - PriorityBlockingQueue
                우선순위가 높은 요소를 먼저 처리하는 블로킹 큐
            - SynchronousQueue
                데이터를 저장하지 않는 블로킹 큐로 생산자가 데이터를 추가하면 소비자가 그 데이터를 받을 때까지 대기한다.
                생산자-소비자 간의 직접적인 핸드오프(hand-off) 메커니즘을 제공한다.
                쉽게 이야기해서 중간에 큐 없이 생산자, 소비자가 직접 거래한다.
            - DelayQueue
                지연된 요소를 처리하는 블로킹 큐로 각 요소는 지정된 지연 시간이 지난 후에야 소비될 수 있다.
                일정 시간이 지난 후 작업을 처리해야 하는 스케줄링 작업에 사용된다.
    */
}
