package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityDemo {
    private static class SyncedIncrement {
        volatile int value = 0;

        public synchronized int get() {
            return value;
        }
        public synchronized void set(int i) {
            value = i;
        }
        public synchronized void add(int i) {
            set(get() + i);
        }
    }

    final static ExecutorService pool = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10000);
        SyncedIncrement demo = new SyncedIncrement();
        for (int i = 0; i < 10000; i++) {
            pool.submit(() -> {
                demo.add(1);
                latch.countDown();
            });
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

