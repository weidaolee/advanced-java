package explicitlock;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public abstract class ReentrantSpinLock implements Lock {
    private static final AtomicReference<Thread> owner = new AtomicReference<>();
    private int count = 0;

    @Override
    public void lock() {
        Thread t = Thread.currentThread();
        if (t == owner.get()) {
            count ++;
            return;
        }
        while (owner.compareAndSet(null, t)) {
            Thread.yield();
        }
    }

    @Override
    public void unlock() {
        Thread t = Thread.currentThread();
        if (t == owner.get()) {
            if (count > 0) {
                count --;
            } else {
                owner.set(null);
            }
        }
    }
}
