package thread.collection.simple;

import static util.MyLogger.log;

public class SimpleListMainV2 {
    public static void main(String[] args) throws InterruptedException {
        //test(new SimpleListV1());
        //test(new SimpleListV2());
        test(new SimpleListV3(new SimpleListV1()));
    }

    private static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        Runnable r1 = () -> {
            list.add("A");
            log("t1 add A");
        };

        Runnable r2 = () -> {
            list.add("B");
            log("t2 add B");
        };

        Thread t1 = new Thread(r1, "t1");
        Thread t2 = new Thread(r2, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log(list);
    }
}
