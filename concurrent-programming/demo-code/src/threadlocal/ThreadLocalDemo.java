package threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class ThreadLocalDemo {
    static class LocalData {
        static final AtomicInteger AMOUNT = new AtomicInteger(0);
        int id = 0;
        int data = 0;
        public LocalData() {
            id = AMOUNT.incrementAndGet();
        }
        @Override
        public String toString() {
            return id + "@LocalData{data = " + data + "}";
        }
    }

    private static final ThreadLocal<LocalData> LOCAL_DATA = new ThreadLocal<>();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    if (LOCAL_DATA.get() == null)
                        LOCAL_DATA.set(new LocalData());
                    System.out.println("Get Initial Thread Local Data:" + LOCAL_DATA.get());
                    for (int j = 0; j < 10; j++) {
                        LocalData localData = LOCAL_DATA.get();
                        localData.data += 1;
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Add Local Data 10 Times:" + LOCAL_DATA.get());
                    LOCAL_DATA.remove();
                }
            });
        }
        pool.shutdown();
    }
}
