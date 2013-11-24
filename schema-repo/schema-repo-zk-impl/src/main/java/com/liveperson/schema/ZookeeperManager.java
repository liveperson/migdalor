package com.liveperson.schema;

import com.liveperson.migdalor.curator.CuratorFactory;
import com.liveperson.migdalor.curator.api.MigdalorCuratorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 6/6/13
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ZookeeperManager {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperManager.class);

    private String hosts;

    public ZookeeperManager(String hosts) {
        this.hosts = hosts;
    }


    public String getDataAndDisconnect(String path) {
        MigdalorCuratorClient migdalorCuratorClient = createCurator();

        String data = null;
        try {
            data = migdalorCuratorClient.getData(path);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            closeCurator(migdalorCuratorClient);
        }
        return data;
    }

    public void setDataAndDisconnect(String path, String data) {
        MigdalorCuratorClient migdalorCuratorClient = createCurator();

        try {
            if (!migdalorCuratorClient.isExist(path)) {
                migdalorCuratorClient.create(path, data);
            } else {
                migdalorCuratorClient.setData(path, data);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            closeCurator(migdalorCuratorClient);
        }
    }

    public List<String> getChildrenAndDisconnect(String path) {
        MigdalorCuratorClient migdalorCuratorClient = createCurator();

        List<String> data = null;
        try {
            data = migdalorCuratorClient.getChildren(path);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            closeCurator(migdalorCuratorClient);
        }
        return data;
    }

    public void writePath(String path) {
        MigdalorCuratorClient migdalorCuratorClient = createCurator();

        try {
            if (!migdalorCuratorClient.isExist(path)) {
                migdalorCuratorClient.create(path);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            closeCurator(migdalorCuratorClient);
        }
    }

    private MigdalorCuratorClient createCurator() {
        return  CuratorFactory.create(hosts);
    }

    private void closeCurator(MigdalorCuratorClient curator) {
        curator.close();
    }
}
