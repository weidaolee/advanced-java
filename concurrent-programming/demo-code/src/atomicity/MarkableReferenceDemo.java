package atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicMarkableReference;

import static java.lang.Thread.sleep;

public class MarkableReferenceDemo {
    static AtomicMarkableReference<Integer> refInteger = new AtomicMarkableReference<>(0, false);
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        pool.submit(() -> {
            boolean success = false;
            boolean marked = !refInteger.isMarked();
            System.out.println("B: before 500ms, " + "ref:" + refInteger.getReference() + ", marked:" + refInteger.isMarked());
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = refInteger.compareAndSet(0, 1, !marked, marked);
            System.out.println("B: after 500ms, CAS(0, 1):" + success + ", ref:" + refInteger.getReference() + ", marked:" + refInteger.isMarked());

            success = refInteger.compareAndSet(1, 0, marked, marked);
            System.out.println("B: after 500ms, CAS(1, 0):" + success + ", ref:" + refInteger.getReference() + ", marked:" + refInteger.isMarked());

            latch.countDown();
        });
        pool.submit(() -> {
            boolean success = false;
            boolean marked = !refInteger.isMarked();
            System.out.println("A: before 1000ms, " + "ref:" + refInteger.getReference() + ", marked:" + refInteger.isMarked());
            System.out.println();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = refInteger.compareAndSet(0, 1, !marked, marked);
            System.out.println("A: after 1000ms, CAS(0, 1):" + success + ", ref:" + refInteger.getReference() + ", marked:" + refInteger.isMarked());
            latch.countDown();
        });
        pool.shutdown();
    }
}
