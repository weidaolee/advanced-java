package explicitlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    final static ReadWriteLock lock = new ReentrantReadWriteLock();
    final static Map<Integer, String> map = new HashMap<>();
    public static void put(int key, String val) {
        lock.writeLock().lock();
        try {
            map.put(key, val);
        } finally {
            lock.writeLock().unlock();
        }
    }
    public static String get(int key) {
        lock.readLock().lock();
        try {
            if (map.containsKey(key)) {
                return map.get(key);
            } else {
                throw new RuntimeException("No this key");
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
