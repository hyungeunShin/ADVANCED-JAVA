package thread.start.test;

import util.MyLogger;

public class StartTest4Main {
    public static void main(String[] args) {
        Thread threadA = new Thread(new CounterRunnable("A", 1000), "Thread-A");
        Thread threadB = new Thread(new CounterRunnable("B", 500), "Thread-B");

        threadA.start();
        threadB.start();
    }

    static class CounterRunnable implements Runnable {
        private final String message;
        private final int time;

        public CounterRunnable(String message, int time) {
            this.message = message;
            this.time = time;
        }

        @Override
        public void run() {
            while(true) {
                MyLogger.log(message);
                try {
                    Thread.sleep(time);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
