package explicitlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class SemaphoreReadWriteDemo {
    private final Semaphore semaphore = new Semaphore(65536, true);
    volatile int value = 0;
    int getValue() throws InterruptedException {
        long start = System.currentTimeMillis();
        semaphore.acquire();
        int res = value;
        sleep(1000);
        semaphore.release();
        long time = System.currentTimeMillis() - start;
        System.out.println("get value spent: " + time);
        return res;
    }
    void update() throws InterruptedException {
        long start = System.currentTimeMillis();
        semaphore.acquire(65536);
        value ++;
        sleep(1000);
        semaphore.release(65536);
        long time = System.currentTimeMillis() - start;
        System.out.println("update spent: " + time);
    }

    public static void main(String[] args) {
        SemaphoreReadWriteDemo demo = new SemaphoreReadWriteDemo();
        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(9);
        for (int i = 0; i < 3; i++) {
            pool.submit(() -> {
                try {
                    System.out.println("get: " + demo.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            latch.countDown();
        }
        for (int i = 0; i < 3; i++) {
            pool.submit(() -> {
                try {
                    demo.update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            latch.countDown();
        }

        for (int i = 0; i < 3; i++) {
            pool.submit(() -> {
                try {
                    System.out.println("get: " + demo.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            latch.countDown();
        }
        pool.shutdown();
    }
}
