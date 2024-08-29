package thread.executor.test;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class OrderService {
    public static void main(String[] args) {
        order("order111");
    }

    public static void order(String orderNo) {
        try(ExecutorService es = Executors.newFixedThreadPool(3)) {
            Future<Boolean> inventoryWork = es.submit(new InventoryWork(orderNo));
            Future<Boolean> shippingWork = es.submit(new ShippingWork(orderNo));
            Future<Boolean> accountingWork = es.submit(new AccountingWork(orderNo));

            Boolean inventoryResult = inventoryWork.get();
            Boolean shippingResult = shippingWork.get();
            Boolean accountingResult = accountingWork.get();

            if(inventoryResult && shippingResult && accountingResult) {
                log("모든 주문 처리가 성공적으로 완료되었습니다.");
            } else {
                log("일부 작업이 실패했습니다.");
            }
        } catch(ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class InventoryWork implements Callable<Boolean> {
        private final String orderNo;

        public InventoryWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("재고 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class ShippingWork implements Callable<Boolean> {
        private final String orderNo;

        public ShippingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("배송 시스템 알림: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class AccountingWork implements Callable<Boolean> {
        private final String orderNo;

        public AccountingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("회계 시스템 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }
}
