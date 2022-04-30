package explicitlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class ConditionDemo {
    static Lock lock = new ReentrantLock();
    static private final Condition condition = lock.newCondition();

    private static class WaitThread implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println("I am WaitThread, start waiting...");
                condition.await();
                System.out.println("I am WaitThread, I was notified!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
    private static class NotifyThread implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println("I am NotifyThread, now I want to do something in critical section...");
                sleep(1000);
                System.out.println("I am NotifyThread, now I notify the WaitThread, but I still hava lock...");
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("I am NotifyThread, now I release the lock...");
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new WaitThread());
        Thread notifyThread = new Thread(new NotifyThread());

        waitThread.start();
        sleep(1000);
        notifyThread.start();
    }
}
