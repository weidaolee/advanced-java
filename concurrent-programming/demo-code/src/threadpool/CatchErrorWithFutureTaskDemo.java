package threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CatchErrorWithFutureTaskDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.execute(new TargetTaskWithError());
        Future<?> future = pool.submit(new TargetTaskWithError());
        try {
            if (future.get() == null) {
                System.out.println("task done.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
