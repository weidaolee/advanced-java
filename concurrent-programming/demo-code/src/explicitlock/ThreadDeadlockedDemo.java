package explicitlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class ThreadDeadlockedDemo {
    static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public static void main(String[] args) throws InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Runnable task1 = () -> Demo.twoLocksTask(lock1, lock2);
        Runnable task2 = () -> Demo.twoLocksTask(lock2, lock1);

        Thread t1 = new Thread(task1, "T1");
        Thread t2 = new Thread(task2, "T2");

        t1.start();
        t2.start();

        sleep(2000);
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads.length > 0) {
            System.out.println("Deadlock detected, report the info:");
            for (long pid : deadlockedThreads) {
                ThreadInfo info = threadMXBean.getThreadInfo(pid, Integer.MAX_VALUE);
                System.out.println(info);
            }
            System.out.println("Interrupt a thread to solve the deadlock.");
            t1.interrupt();
        }
    }
    private static class Demo {
        static void twoLocksTask(Lock lock1, Lock lock2) {
            Thread t = Thread.currentThread();

            String lock1Name = lock1.toString().replace("java.util.concurrent.locks.", "");
            String lock2Name = lock1.toString().replace("java.util.concurrent.locks.", "");

            System.out.println(t.getName() + " start locking the first lock:" + lock1Name);
            try {
                lock1.lockInterruptibly();
            } catch (InterruptedException e) {
                System.out.println(t.getName() + " was interrupted, " + "lock " + lock1Name + " faild.");
                e.printStackTrace();
            }
            try {
                System.out.println(t.getName() + " lock " + lock1Name + "successfully.");
                System.out.println(t.getName() + " start locking the second lock:" + lock2Name);
                try {
                    lock2.lockInterruptibly();
                } catch (InterruptedException e) {
                    System.out.println(t.getName() + " was interrupted, " + "lock " + lock2Name + " faild.");
                    e.printStackTrace();
                    return;
                }
                try {
                    System.out.println(t.getName() + " lock " + lock2Name + "successfully.");
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock2.unlock();
                    System.out.println(t.getName() + " release the lock: " + lock2Name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock1.unlock();
                System.out.println(t.getName() + " release the lock: " + lock1Name);
            }
        }
    }
}

