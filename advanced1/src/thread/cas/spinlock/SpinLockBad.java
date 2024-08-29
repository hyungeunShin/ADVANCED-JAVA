package thread.cas.spinlock;

import util.ThreadUtils;

import static util.MyLogger.log;

public class SpinLockBad {
    /*
    두 부분이 원자적이지 않다는 문제가 있다.
        1. 락 사용 여부 확인
        2. 락의 값 변경

    이 둘은 한 번에 하나의 스레드만 실행해야 한다.
    따라서 synchronized 또는 Lock 을 사용해서 두 코드를 동기화해서 안전한 임계 영역을 만들어야 한다.
    여기서 다른 해결 방안도 있다. 바로 두 코드를 하나로 묶어서 원자적으로 처리하는 것이다.
    CAS 연산을 사용하면 두 연산을 하나로 묶어서 하나의 원자적인 연산으로 처리할 수 있다.
    락의 사용 여부를 확인하고 그 값이 기대하는 값과 같다면 변경하는 것이다.
    */

    private volatile boolean lock = false;

    public void lock() {
        log("락 획득 시도");
        while(true) {
            if(!lock) {
                ThreadUtils.sleep(100);
                lock = true;
                break;
            } else {
                log("락 획득 실패 - 스핀 대기");
            }
        }
        log("락 획득 완료");
    }

    public void unlock() {
        lock = false;
        log("락 반납 완료");
    }
}
