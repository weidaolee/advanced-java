package explicitlock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public abstract class SpinLock implements Lock {
    private static final AtomicReference<Thread> onwer = new AtomicReference<>();

    @Override
    public void lock() {
        Thread t = Thread.currentThread();
        while (onwer.compareAndSet(null, t)) {
            Thread.yield();
        }
    }

    @Override
    public void unlock() {
        Thread t = Thread.currentThread();
        if (t == onwer.get()) {
            onwer.set(null);
        }
    }
}
