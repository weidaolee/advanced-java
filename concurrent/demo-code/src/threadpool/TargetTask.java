package threadpool;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class TargetTask implements Runnable {
    public static final int SLEEP_GAP = 500;
    static AtomicInteger taskID = new AtomicInteger(1);
    protected String taskName;
    public TargetTask (){
        taskName = "task-" + taskID.get();
        taskID.incrementAndGet();
    }
    @Override
    public void run() {
        System.out.println("Task: " + taskName + " doing...");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(taskName + " done.");
    }
}
