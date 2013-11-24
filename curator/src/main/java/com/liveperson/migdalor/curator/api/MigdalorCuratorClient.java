package com.liveperson.migdalor.curator.api;

import com.netflix.curator.framework.CuratorFramework;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kobis
 * Date: 9/25/13
 * Time: 8:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MigdalorCuratorClient {

    public void create(String path) throws Exception;

    public void create(String path, String payload) throws Exception;

    public void setData(String path, String payload) throws Exception;

    public String getData(String path) throws Exception;

    public void delete(String path) throws Exception;

    public List<String> getChildren(String path) throws Exception;

    public List<String> setWatcherGetChildren(String path, Watcher watcher) throws Exception;

    public void close();

    public CuratorFramework getClient();

    boolean isExist(String path) throws Exception;
}
