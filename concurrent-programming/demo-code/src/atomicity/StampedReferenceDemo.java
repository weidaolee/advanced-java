package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicStampedReference;

import static java.lang.Thread.sleep;


public class StampedReferenceDemo {
    static AtomicStampedReference<Integer> refInteger = new AtomicStampedReference<>(0, 0);
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        pool.submit(() -> {
            boolean success = false;
            int stamp = refInteger.getStamp();
            System.out.println("B: before 500ms, " + "ref:" + refInteger.getReference() + ". stamp:" + refInteger.getStamp());
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = refInteger.compareAndSet(0, 1, stamp, stamp + 1);
            System.out.println("B: after 500ms, CAS(0, 1):" + success + ", ref:" + refInteger.getReference() + ". stamp:" + refInteger.getStamp());

            stamp ++;
            success = refInteger.compareAndSet(1, 0, stamp, stamp + 1);
            System.out.println("B: after 500ms, CAS(1, 0):" + success + ", ref:" + refInteger.getReference() + ". stamp:" + refInteger.getStamp());

            latch.countDown();
        });
        pool.submit(() -> {
            boolean success = false;
            int stamp = refInteger.getStamp();
            System.out.println("A: before 1000ms, " + "ref:" + refInteger.getReference() + ". stamp:" + refInteger.getStamp());
            System.out.println();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = refInteger.compareAndSet(0, 1, stamp, stamp + 1);
            System.out.println("A: after 1000ms, CAS(0, 1):" + success + ", ref:" + refInteger.getReference() + ". stamp:" + refInteger.getStamp());
            latch.countDown();
        });
        pool.shutdown();
    }
}
