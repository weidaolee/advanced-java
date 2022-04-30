package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileDemo {
    private static class SyncedIncrement {
        int value = 0;

        public int get() {
            return value;
        }
        public void set(int i) {
            value = i;
        }
        public void add(int i) {
            set(get() + i);
        }
    }

    final static ExecutorService pool = Executors.newFixedThreadPool(10000);
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10000);
        SyncedIncrement demo = new SyncedIncrement();
        for (int i = 0; i < 10000; i++) {
            pool.submit(() -> {demo.add(1);});
            latch.countDown();
        }
        pool.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result: " + demo.get());
    }
}
