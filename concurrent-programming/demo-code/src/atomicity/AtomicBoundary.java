package atomicity;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicBoundary {
    private final AtomicReference<SafeBoundary> refBoundary = new AtomicReference<>(new SafeBoundary(Integer.MIN_VALUE, Integer.MAX_VALUE));

    static class SafeBoundary {
        final int lower;
        final int upper;

        SafeBoundary(int lower, int upper) {
            if (lower > upper)
                throw new IllegalArgumentException();
            this.lower = lower;
            this.upper = upper;
        }

        SafeBoundary(SafeBoundary boundary) {
            this.lower = boundary.lower;
            this.upper = boundary.upper;
        }
    }

    public void setBoundary(int lower, int upper){
        boolean success;
        SafeBoundary curBoundary;
        SafeBoundary newBoundary = new SafeBoundary(lower, upper);
        do {
            curBoundary = refBoundary.get();
            Thread.yield();
            success = refBoundary.compareAndSet(curBoundary, newBoundary);
            if (!success)
                System.out.println("Thread " + Thread.currentThread().getName() + " CAS faild");
        } while (!success);
    }
    public SafeBoundary getBoundary() {
        return new SafeBoundary(refBoundary.get());
    }
}
