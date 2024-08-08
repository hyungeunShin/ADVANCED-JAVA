package thread.start.test;

import util.MyLogger;

public class StartTest3Main {
    public static void main(String[] args) {
        /*
        Thread thread = new Thread(() -> {
            for(int i = 1; i < 6; i++) {
                MyLogger.log("value : " + i);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "counter");
        thread.start();
        */

        Runnable runnable = () -> {
            for(int i = 1; i < 6; i++) {
                MyLogger.log("value : " + i);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(runnable, "counter");
        thread.start();
    }
}
