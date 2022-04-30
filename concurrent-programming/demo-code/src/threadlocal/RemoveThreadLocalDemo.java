package threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RemoveThreadLocalDemo {
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(
            2,
            4,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2)) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                START_TIME.remove();
            }
        };
    }
}
