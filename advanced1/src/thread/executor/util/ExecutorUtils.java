package thread.executor.util;

import util.MyLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class ExecutorUtils {
    public static void printState(ExecutorService executorService) {
        if(executorService instanceof ThreadPoolExecutor poolExecutor) {
            //스레드 풀에서 관리되는 스레드의 숫자
            int pool = poolExecutor.getPoolSize();
            //작업을 수행하는 스레드의 숫자
            int active = poolExecutor.getActiveCount();
            //큐에 대기중인 작업의 숫자
            int queuedTasks = poolExecutor.getQueue().size();
            //완료된 작업의 숫자
            long completedTask = poolExecutor.getCompletedTaskCount();
            MyLogger.log("[pool : " + pool + "] [active : " + active + "] [queuedTasks : " + queuedTasks + "] [completedTasks : " + completedTask + "]");
        } else {
            MyLogger.log(executorService);
        }
    }

    public static void printState(ExecutorService executorService, String taskName) {
        if(executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int active = poolExecutor.getActiveCount();
            int queuedTasks = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            MyLogger.log(taskName + " -> [pool : " + pool + "] [active : " + active + "] [queuedTasks : " + queuedTasks + "] [completedTasks : " + completedTask + "]");
        } else {
            MyLogger.log(taskName + " -> " + executorService);
        }
    }
}
