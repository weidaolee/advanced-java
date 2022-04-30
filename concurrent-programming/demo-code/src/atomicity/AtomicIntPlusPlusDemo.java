package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntPlusPlusDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger a = new AtomicInteger(0);
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    a.getAndIncrement();
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("a:" + a.get());
    }
}
