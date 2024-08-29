package thread.collection.simple;

import util.ThreadUtils;

import java.util.Arrays;

public class SimpleListV2 implements SimpleList {
    /*
    BasicList 코드에 synchronized 기능만 추가한 SyncList 를 만들었다.
    동시성 문제를 해결했지만 이렇게 되면 모든 컬렉션을 다 복사해서 동기화용으로 새로 구현해야 한다. 이것은 매우 비효율적이다.
    */

    private static final int DEFAULT_CAPACITY = 5;
    private final Object[] elementData;
    private int size;

    public SimpleListV2() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public synchronized int size() {
        return size;
    }

    @Override
    public synchronized void add(Object e) {
        elementData[size] = e;
        ThreadUtils.sleep(100);
        size++;
    }

    @Override
    public synchronized Object get(int index) {
        return elementData[index];
    }

    @Override
    public synchronized String toString() {
        return Arrays.toString(Arrays.copyOf(elementData, size)) + " size : " + size + ", capacity : " + elementData.length;
    }
}
