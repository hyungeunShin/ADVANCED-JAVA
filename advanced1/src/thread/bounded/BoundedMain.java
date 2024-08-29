package thread.bounded;

import java.util.ArrayList;
import java.util.List;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedMain {
    public static void main(String[] args) {
        //BoundedQueue queue = new BoundedQueueV1(2);
        //BoundedQueue queue = new BoundedQueueV2(2);
        //BoundedQueue queue = new BoundedQueueV3(2);
        //BoundedQueue queue = new BoundedQueueV4(2);
        //BoundedQueue queue = new BoundedQueueV5(2);
        //BoundedQueue queue = new BoundedQueueV6_1(2);
        //BoundedQueue queue = new BoundedQueueV6_2(2);
        //BoundedQueue queue = new BoundedQueueV6_3(2);
        BoundedQueue queue = new BoundedQueueV6_4(2);

        producerFirst(queue);
        //consumerFirst(queue);
    }

    private static void producerFirst(BoundedQueue queue) {
        log("== [생산자 먼저 실행] 시작 : " + queue.getClass().getSimpleName() + " ==");
        
        List<Thread> list = new ArrayList<>();
        startProducer(queue, list);
        printAllState(queue, list);
        startConsumer(queue, list);
        printAllState(queue, list);

        log("== [생산자 먼저 실행] 종료 : " + queue.getClass().getSimpleName() + " ==");
    }

    private static void consumerFirst(BoundedQueue queue) {
        log("== [소비자 먼저 실행] 시작 : " + queue.getClass().getSimpleName() + " ==");
        
        List<Thread> list = new ArrayList<>();
        startConsumer(queue, list);
        printAllState(queue, list);
        startProducer(queue, list);
        printAllState(queue, list);

        log("== [소비자 먼저 실행] 종료 : " + queue.getClass().getSimpleName() + " ==");
    }

    private static void startProducer(BoundedQueue queue, List<Thread> list) {
        System.out.println();
        log("생산자 시작");
        for(int i = 1; i <= 3; i++) {
            Thread producer = new Thread(new ProducerTask(queue, "data" + i), "producer" + i);
            list.add(producer);
            producer.start();
            //이해를 돕기 위해 0.1초의 간격으로 sleep 을 주면서 순차적으로 실행
            sleep(100);
        }
    }

    private static void startConsumer(BoundedQueue queue, List<Thread> list) {
        System.out.println();
        log("소비자 시작");
        for(int i = 1; i <= 3; i++) {
            Thread consumer = new Thread(new ConsumerTask(queue), "consumer" + i);
            list.add(consumer);
            consumer.start();
            //이해를 돕기 위해 0.1초의 간격으로 sleep 을 주면서 순차적으로 실행
            sleep(100);
        }
    }

    private static void printAllState(BoundedQueue queue, List<Thread> list) {
        System.out.println();
        log("현재 상태 출력, 큐 데이터 : " + queue);
        for(Thread thread : list) {
            log(thread.getName() + " : " + thread.getState());
        }
    }
}
