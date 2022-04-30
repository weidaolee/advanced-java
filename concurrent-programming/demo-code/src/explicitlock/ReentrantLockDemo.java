package explicitlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    static Lock lock = new ReentrantLock();
    volatile int value;

    public int get () {
        lock.lock();
        try {
            return value;
        } finally {
            lock.unlock();
        }
    }
    public void add(int value) {
        lock.lock();
        try {
            this.value = get() + value;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
                demo.add(1);
            });
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
