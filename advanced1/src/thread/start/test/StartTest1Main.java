package thread.start.test;

import util.MyLogger;

public class StartTest1Main {
    public static void main(String[] args) {
        CounterThread thread = new CounterThread();
        thread.start();
    }

    static class CounterThread extends Thread {
        @Override
        public void run() {
            for(int i = 1; i < 6; i++) {
                MyLogger.log("value : " + i);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
