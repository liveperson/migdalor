package com.liveperson.migdalor.curator;

import com.liveperson.migdalor.curator.impl.MigdalorCuratorClientImpl;
import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;

/**
 * Created with IntelliJ IDEA.
 * User: kobis
 * Date: 9/25/13
 * Time: 8:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class CuratorFactory {

    public static final int RETRIES = 3;
    public static final int SLEEP_MS_BETWEEN_RETRIES = 100;

    public static MigdalorCuratorClientImpl create(String ZKConnect)
    {
        RetryPolicy retryPolicy = new RetryNTimes(RETRIES, SLEEP_MS_BETWEEN_RETRIES);
        return new MigdalorCuratorClientImpl(CuratorFrameworkFactory.newClient(ZKConnect, retryPolicy));
    }

    public static MigdalorCuratorClientImpl create(String ZKConnect, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs)
    {
        return new MigdalorCuratorClientImpl(
                CuratorFrameworkFactory.builder()
                .connectString(ZKConnect)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build());
    }

}
