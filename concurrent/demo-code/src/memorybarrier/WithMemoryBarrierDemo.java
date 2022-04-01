package memorybarrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WithMemoryBarrierDemo implements Runnable{
    private volatile int x = 0;
    private volatile boolean isModified = false;

    public void update() {
        x = 1;
        isModified = true;
    }

    public void reset() {
        x = 0;
        isModified = false;
    }
    public void show() {
        if(isModified) {
            System.out.println("x: " + x);
        }
    }

    @Override
    public void run() {
        update();
        show();
        reset();
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        WithMemoryBarrierDemo task = new WithMemoryBarrierDemo();
        for (int i = 0; i < 1000; i++) {
            pool.submit(task);
        }
        pool.shutdown();
    }
}

