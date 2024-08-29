package thread.collection.java;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class SynchronizedMain {
    public static void main(String[] args) {
        List<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);

        Set<Integer> copySet = new CopyOnWriteArraySet<>();
        copySet.add(1);
        copySet.add(2);
        copySet.add(3);
        System.out.println(copySet);

        Set<Integer> skipSet = new ConcurrentSkipListSet<>();
        skipSet.add(1);
        skipSet.add(2);
        skipSet.add(3);
        System.out.println(skipSet);

        Map<Integer, String> map1 = new ConcurrentHashMap<>();
        map1.put(1, "data1");
        map1.put(2, "data2");
        map1.put(3, "data3");
        System.out.println(map1);

        Map<Integer, String> map2 = new ConcurrentSkipListMap<>();
        map2.put(1, "data1");
        map2.put(2, "data2");
        map2.put(3, "data3");
        System.out.println(map2);
    }
}
