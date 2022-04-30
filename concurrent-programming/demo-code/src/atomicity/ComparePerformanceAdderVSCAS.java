package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class ComparePerformanceAdderVSCAS {
    public static void main(String[] args) {
        System.out.println("AtomicLong:");
        TestAtomicLongPerformance.test();
        System.out.println();
        System.out.println("LongAdder:");
        TestLongAdderPerformance.test();
    }
}

class TestAtomicLongPerformance {
    public static void test() {
        final int TURNS = 100000000;
        final int TASKS = 10;

        ExecutorService pool = Executors.newCachedThreadPool();
        AtomicLong x = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(TASKS);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASKS; i++) {
            pool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    x.incrementAndGet();
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        float time = (System.currentTimeMillis() - start) / 1000F;
        System.out.println("execution time: " + time);
        System.out.println("result: " + x.get());

    }
}

class TestLongAdderPerformance {
    public static void test() {
        final int TURNS = 100000000;
        final int TASKS = 10;

        ExecutorService pool = Executors.newCachedThreadPool();
        LongAdder x = new LongAdder();
        CountDownLatch latch = new CountDownLatch(TASKS);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASKS; i++) {
            pool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    x.add(1);
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        float time = (System.currentTimeMillis() - start) / 1000F;
        System.out.println("execution time: " + time);
        System.out.println("result: " + x.longValue());
    }
}
