package atomicity;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class CASPlusPlusDemo {
    private static final int THREAD_NUM = 10;
    private static final Unsafe unsafe = getUnsafe();
    private volatile int attr = 0;
    private static long attrOffset;
    private static final AtomicLong failure = new AtomicLong(0);
    static {
        try {
            attrOffset = unsafe.objectFieldOffset(CASPlusPlusDemo.class.getDeclaredField("attr"));
            System.out.println("attribute offset:" + attrOffset);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public final boolean unSafeCompareAndSet(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, attrOffset, oldValue, newValue);
    }

    public void selfPlus() {
        int oldValue = attr;
        int i = 0;
        do {
            if (i++ > 1) {
                failure.incrementAndGet();
            }
        } while (!unSafeCompareAndSet(oldValue, oldValue + 1));
    }

    public static void main(String[] args) throws InterruptedException {
        final CASPlusPlusDemo casPlusPlusDemo = new CASPlusPlusDemo();
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < THREAD_NUM; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    casPlusPlusDemo.selfPlus();
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("result:" + casPlusPlusDemo.attr);
        System.out.println("failure:" + CASPlusPlusDemo.failure.get());
    }

    public static Unsafe getUnsafe()  {
        Field theUnsafe = null;
        try {
            theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        theUnsafe.setAccessible(true);
        try {
            return (Unsafe) theUnsafe.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
