package memorybarrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class NoMemoryBarrierDemo {
    private int x = 0;
    private boolean isModified = false;

    public void update() {
        isModified = true;
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        x = 1;
    }
    public void show() {
        if(isModified) {
            System.out.println("x: " + x);
        }
    }
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        NoMemoryBarrierDemo demo = new NoMemoryBarrierDemo();
        pool.submit(demo::update);
        pool.submit(demo::show);
        pool.shutdown();
    }
}
