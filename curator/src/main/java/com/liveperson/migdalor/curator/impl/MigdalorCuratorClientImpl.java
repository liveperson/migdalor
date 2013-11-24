package com.liveperson.migdalor.curator.impl;

import com.liveperson.migdalor.curator.api.MigdalorCuratorClient;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.api.ExistsBuilder;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kobis
 * Date: 9/25/13
 * Time: 8:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class MigdalorCuratorClientImpl implements MigdalorCuratorClient {

    private static final Charset utf8 = Charset.forName("UTF-8");

    private CuratorFramework client;

    public MigdalorCuratorClientImpl(CuratorFramework client) {
        this.client = client;
        client.start();
    }

    @Override
    public void create(String path) throws Exception
    {
        client.create().creatingParentsIfNeeded().forPath(path);
    }

    @Override
    public void create(String path, String payload) throws Exception
    {
        client.create().creatingParentsIfNeeded().forPath(path, payload.getBytes());
    }

    @Override
    public void setData(String path, String payload) throws Exception
    {
        client.setData().forPath(path, payload.getBytes());
    }

    @Override
    public String getData(String path) throws Exception {
        return new String(client.getData().forPath(path), utf8);
    }

    @Override
    public void delete(String path) throws Exception
    {
        client.delete().guaranteed().forPath(path);
    }

    @Override
    public List<String> getChildren(String path) throws Exception
    {
        return client.getChildren().forPath(path);
    }

    @Override
    public List<String> setWatcherGetChildren(String path, Watcher watcher) throws Exception
    {
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }

    @Override
    public boolean isExist(String path) throws Exception
    {
        return (client.checkExists().forPath(path) != null);
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public CuratorFramework getClient() {
        return client;
    }
}
