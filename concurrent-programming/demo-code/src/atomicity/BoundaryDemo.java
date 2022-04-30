package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoundaryDemo {
    static ExecutorService pool = Executors.newCachedThreadPool();
    static AtomicBoundary boundary = new AtomicBoundary();

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                int lower = (int) (Math.random() * 100);
                int upper = lower + (int) (Math.random() * 100);
                boundary.setBoundary(lower, upper);
            });
            latch.countDown();
        }
        pool.shutdown();
    }
}
