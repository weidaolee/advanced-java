package explicitlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract public class ReadWriteLockLazyLoadCache<K,V> {
    final Map<K,V> cache = new HashMap<>();
    final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    final Lock rlock = rwlock.readLock();
    final Lock wlock = rwlock.writeLock();

    V get(K key) {
        V val = null;
        rlock.lock();
        try {
            val = cache.get(key);
        } finally {
            rlock.unlock();
        }

        if (val != null) {
            return val;
        }

        wlock.lock();
        try {
            val = cache.get(key);
            if (val != null) {
                return val;
            }
            val = loadFromDatabase(key);
            cache.put(key, val);
        } finally {
            wlock.unlock();
        }
        return val;
    }
    abstract V loadFromDatabase(K key);
}
