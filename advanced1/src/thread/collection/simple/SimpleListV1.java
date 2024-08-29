package thread.collection.simple;

import util.ThreadUtils;

import java.util.Arrays;

public class SimpleListV1 implements SimpleList {
    /*
    스레드1이 약간 빠르게 수행했다고 가정하겠다.
    스레드1 수행: elementData[0] = A, elementData[0] 의 값은 A가 된다.
    스레드2 수행: elementData[0] = B, elementData[0] 의 값은 A -> B가 된다.
    결과적으로 elementData[0] 의 값은 B가 된다.
    그렇기 때문에 size 는 2인데 데이터는 B 하나만 입력되어 있다.
    */

    private static final int DEFAULT_CAPACITY = 5;
    private final Object[] elementData;
    private int size;

    public SimpleListV1() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(Object e) {
        elementData[size] = e;
        ThreadUtils.sleep(100);
        size++;
    }

    @Override
    public Object get(int index) {
        return elementData[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elementData, size)) + " size : " + size + ", capacity : " + elementData.length;
    }
}
