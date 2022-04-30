package explicitlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierDemo {
    CyclicBarrier cyclicBarrier;
    CountDownLatch latch;
    ExecutorService pool = Executors.newSingleThreadExecutor();
}
