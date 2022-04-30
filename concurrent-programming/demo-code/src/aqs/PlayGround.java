package aqs;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayGround {
    AbstractQueuedSynchronizer a;
    ReentrantLock lock;
    CyclicBarrier barrier;
    ThreadPoolExecutor pool;
}
