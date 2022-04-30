package explicitlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class InterruptableLockDemo {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Runnable task = () -> SyncData.lockInterruptiblyUpdate(lock);
        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");

        t1.start();
        t2.start();
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        t2.interrupt();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result: " + SyncData.value);
    }

    private static class SyncData {
        public static int value = 0;
        public static void lockInterruptiblyUpdate(Lock lock) {
            Thread t = currentThread();
            System.out.println(t.getName() + " start locking...");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                System.out.println(t.getName() + " was interrupted, lock faild.");
                e.printStackTrace();
                return;
            }
            try {
                System.out.println(t.getName() + " lock succesfully! Get into the critical section.");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                value ++;
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(t.getName() + " was interrupted in execution.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
