package threadpool;

import java.util.concurrent.*;

public class GetReturnableTaskResultDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newScheduledThreadPool(1);
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return (int) (Math.random() * 10);
            }
        });
        try {
            int result = future.get();
            System.out.println("retuned result:" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}
