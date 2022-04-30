package explicitlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

abstract public class StampedLockLazyLoadCache <K,V> {
    final StampedLock stampedLock = new StampedLock();
    final Map<K,V> cache = new HashMap<>();

    V get(K key) {
        V val;
        long rstamp;
        long wstamp;
        // try optimistic read
        // nolock
        rstamp = stampedLock.tryOptimisticRead();
        if (rstamp != 0) {
            val = cache.get(key);
            if (val != null) {
                // validate cache data
                if (!stampedLock.validate(rstamp)) {
                    // cache data is dirty
                    // pessmistic read cache or load from database
                    val = pessmisticReadCacheOrLoadFromDatabase(key);
                }
                return val;
            }
            // cache miss
            // lock write lock
            wstamp = stampedLock.writeLock();
            try {
                // double check
                val = cache.get(key);
                if (val != null)
                    return val;
                // cache miss
                // load from database
                val = loadFromDatabase(key);

                // update cache
                cache.put(key, val);
                return val;
            } finally {
                stampedLock.unlockWrite(wstamp);
            }
        }
        return pessmisticReadCacheOrLoadFromDatabase(key);
    }

    V pessmisticReadCacheOrLoadFromDatabase(K key) {
        V val;
        long rstamp = stampedLock.readLock();
        try {
            val = cache.get(key);
            if (val != null)
                return val;
            // cache miss
            // lock write lock
            long wstamp = stampedLock.writeLock();
            try {
                // double check
                val = cache.get(key);
                if (val != null)
                    return val;

                // cache miss
                // load from database
                val = loadFromDatabase(key);

                // update cache
                cache.put(key, val);
                return val;
            } finally {
                stampedLock.unlock(wstamp);
            }
        } finally {
            stampedLock.unlockRead(rstamp);
        }
    }
    abstract V loadFromDatabase(K key);
}

