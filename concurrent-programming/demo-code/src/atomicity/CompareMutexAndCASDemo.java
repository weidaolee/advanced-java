package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class CompareMutexAndCASDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("MutexDemo");
        MutexDemo.demo();
        System.out.println("Atomic");
        AtomicDemo.demo();
    }

    private static class MutexDemo{
        public int value = 0;
        public static void demo() throws InterruptedException {
            final ExecutorService pool = Executors.newCachedThreadPool();
            final CountDownLatch latchW = new CountDownLatch(10000);
            final CountDownLatch latchR = new CountDownLatch(10);
            MutexDemo demo = new MutexDemo();
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 10000; i++) {
                pool.submit(() -> {
                    demo.selfIncrement();
                    latchW.countDown();
                });
                if (i % 1000 == 0) {
                    pool.submit(() -> {
                        //demo.get();
                        latchR.countDown();
                    });
                }
            }
            pool.shutdown();
            latchW.await();
            latchR.await();
            float time = (System.currentTimeMillis() - start) / 1000F;
            System.out.println(demo.get());
            System.out.println("execution time: " + time);
        }
        public synchronized void selfIncrement() {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value++;
        }
        public synchronized int get(){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        }
    }
    private static class AtomicDemo{
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        public static void demo() throws InterruptedException {
            final ExecutorService pool = Executors.newCachedThreadPool();
            final CountDownLatch latchW = new CountDownLatch(10000);
            final CountDownLatch latchR = new CountDownLatch(10);
            AtomicDemo demo = new AtomicDemo();
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 10000; i++) {
                pool.submit(() -> {
                    demo.selfIncrement();
                    latchW.countDown();
                });
                if (i % 1000 == 0) {
                    pool.submit(() -> {
                        //demo.get();
                        latchR.countDown();
                    });
                }
            }
            pool.shutdown();
            latchW.await();
            latchR.await();
            float time = (System.currentTimeMillis() - start) / 1000F;
            System.out.println(demo.get());
            System.out.println("execution time: " + time);
        }
        public void selfIncrement(){
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicInteger.getAndIncrement();
        }
        public int get(){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return atomicInteger.get();
        }
    }
}


