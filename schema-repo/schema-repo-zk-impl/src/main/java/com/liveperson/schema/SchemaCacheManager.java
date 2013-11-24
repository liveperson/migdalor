package com.liveperson.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 6/6/13
 * Time: 9:34 AM
 * Singleton for Schema Cache Manager
 */
public class SchemaCacheManager {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaCacheManager.class);
    private String zkConnect;

    private SchemaCacheManager(){}
    private LruCache lruCache;
    private ZookeeperManager zookeeperManager;

    public SchemaCacheManager(String zkConnect) throws RepoException {
        this.zkConnect = zkConnect;
        zookeeperManager = new ZookeeperManager(zkConnect);
        lruCache = new LruCache();
    }



    protected void setLruCache(LruCache lruCache) {
        this.lruCache = lruCache;
    }


    protected void setZookeeperManager(ZookeeperManager zookeeperManager) {
        this.zookeeperManager = zookeeperManager;
    }

    public Object getSchema(Object key) {
        Object obj =  lruCache.get(key);
        if (obj == null && !lruCache.containsKey(key)) {
            String value = null;
            value = zookeeperManager.getDataAndDisconnect(key.toString());
            lruCache.put(key, value);
            obj = value;
        }
        return obj;
    }

    public List<String> getShemasList(Object key){
        return zookeeperManager.getChildrenAndDisconnect(key.toString());
    }

    public void addSchema(Object key, Object value){
        zookeeperManager.setDataAndDisconnect(key.toString(), value.toString());
        lruCache.put(key, value);

    }

    public void writeRoot(String rootPath) {
        zookeeperManager.writePath(rootPath);
    }
}
