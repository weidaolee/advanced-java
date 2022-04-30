package explicitlock;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
    private static class ParkDemoThread extends Thread {
        public ParkDemoThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println( getName() +  " is parked...");
            LockSupport.park();
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(getName() + " is interrupted but will continue execution...");
            } else {
                System.out.println( getName() +  " is unparked...");
            }
            System.out.println( getName() +  " done.");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new ParkDemoThread("T1");
        Thread t2 = new ParkDemoThread("T2");

        t1.start();
        t2.start();

        t1.interrupt();
        LockSupport.unpark(t2);
    }
}
