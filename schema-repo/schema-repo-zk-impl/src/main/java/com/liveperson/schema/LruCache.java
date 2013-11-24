package com.liveperson.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 6/6/13
 * Time: 9:47 AM
 * A simple LRU cache
 */
public class LruCache {
    private static final Logger LOG = LoggerFactory.getLogger(LruCache.class);

    Map cache;
    final int MAX_ENTRIES = 100;

    public LruCache() {
    // Create cache
        cache = new LinkedHashMap(MAX_ENTRIES+1, .75F, true) {
        // This method is called just after a new entry has been added
            public boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
        };

    // If the cache is to be used by multiple threads,
    // the cache must be wrapped with code to synchronize the methods
        cache = (Map)Collections.synchronizedMap(cache);
    }

    public void put(Object key, Object value){
        cache.put(key, value);
    }

    public Object get(Object key){
        return cache.get(key);
    }

    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }
}
