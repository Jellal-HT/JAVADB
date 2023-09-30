package top.yichenghu.javadb.backend.common;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import top.yichenghu.javadb.common.Error;

/**
 * AbstractCache implements a reference counting strategy for caching.
 */
public abstract class AbstractCache<T> {
    private HashMap<Long, T> cache;                     // Actual Cached Data
    private HashMap<Long, Integer> references;          // Element Reference Count
    private HashMap<Long, Boolean> getting;             // Threads acquiring a certain resource

    private int maxResource;                            // Maximum Cached Resource Coun
    private int count = 0;                              // Number of Elements in the Cache
    private Lock lock;

    public AbstractCache(int maxResource) {
        this.maxResource = maxResource;
        cache = new HashMap<>();
        references = new HashMap<>();
        getting = new HashMap<>();
        lock = new ReentrantLock();
    }

    protected T get(long key) throws Exception {
        while(true) {
            lock.lock();
            if(getting.containsKey(key)) {
                // The requested resource is being acquired by another thread
                lock.unlock();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                continue;
            }

            if(cache.containsKey(key)) {
                //Resources are cached and returned directly.
                T obj = cache.get(key);
                references.put(key, references.get(key) + 1);
                lock.unlock();
                return obj;
            }

            // try to acquire resource
            if(maxResource > 0 && count == maxResource) {
                lock.unlock();
                throw Error.CacheFullException;
            }
            count ++;
            getting.put(key, true);
            lock.unlock();
            break;
        }

        T obj = null;
        try {
            obj = getForCache(key);
        } catch(Exception e) {
            lock.lock();
            count --;
            getting.remove(key);
            lock.unlock();
            throw e;
        }

        lock.lock();
        getting.remove(key);
        cache.put(key, obj);
        references.put(key, 1);
        lock.unlock();
        
        return obj;
    }

    /**
     * release a cached resouce
     */
    protected void release(long key) {
        lock.lock();
        try {
            int ref = references.get(key)-1;
            if(ref == 0) {
                T obj = cache.get(key);
                releaseForCache(obj);
                references.remove(key);
                cache.remove(key);
                count --;
            } else {
                references.put(key, ref);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Disable caching, write back all resources
     */
    protected void close() {
        lock.lock();
        try {
            Set<Long> keys = cache.keySet();
            for (long key : keys) {
                T obj = cache.get(key);
                releaseForCache(obj);
                references.remove(key);
                cache.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }


    /**
     * Behavior When a Resource is Not in Cache
     */
    protected abstract T getForCache(long key) throws Exception;
    /**
     * Write-Back Behavior on Cache Eviction
     */
    protected abstract void releaseForCache(T obj);
}
