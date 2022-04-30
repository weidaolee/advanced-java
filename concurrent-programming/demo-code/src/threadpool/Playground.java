package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Playground {
    ExecutorService pool = Executors.newFixedThreadPool(10);
}
