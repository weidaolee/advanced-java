package explicitlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockHappensBeforeDemo {
    final static Lock lock = new ReentrantLock();
    final static ExecutorService pool = Executors.newFixedThreadPool(1000);
    static volatile int value = 0;

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
                lock.lock();
                try {
                    value ++;
                } finally {
                    lock.unlock();
                }
            });
            latch.countDown();
        }
        pool.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result:" + value);
    }
}

