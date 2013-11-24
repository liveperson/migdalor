package com.liveperson.schema;

import com.liveperson.migdalor.curator.util.EmbeddedZK;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 6/4/13
 * Time: 8:29 AM
 * To change this template use File | Settings | File Templates.
 */
//TODO delete this
public class SchemaZkRepoTest {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaZkRepoTest.class);

    EmbeddedZK embeddedZK;
    public static final int zkPort = 2189; // different than the default

    public static final String ZK_URL_VALUE = "localhost:" + zkPort +  "/schemarepo";
    public static final String SCHEMA = "this is a dummy schema";

    @Mock
    ZookeeperManager zookeeperManagerMock;

    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);
        embeddedZK = new EmbeddedZK();
        embeddedZK.setPort(zkPort);
        embeddedZK.start();
    }

    @After
    public void after() throws IOException {
        embeddedZK.stop();
    }


    @Test
    public void addToZkAndReadSchemaFromCache() throws RepoException {
        Properties properties = new Properties();
        properties.put(SchemaZkRepo.ZK_CONNECT_CONNECT, ZK_URL_VALUE);

        SchemaZkRepo schemaRepo = new SchemaZkRepo(properties);
        schemaRepo.addSchema("/1", SCHEMA);
        Object schema = schemaRepo.getSchema("/1");
        assertTrue(schema instanceof String);
        String strSchema = (String) schema;
        assertEquals(SCHEMA, strSchema);

    }
    @Test
    public void readSchemaFromZk() throws RepoException {
        Mockito.when(zookeeperManagerMock.getDataAndDisconnect(anyString())).thenReturn(SCHEMA);
        Properties properties = new Properties();
        properties.put(SchemaZkRepo.ZK_CONNECT_CONNECT, ZK_URL_VALUE);

        SchemaZkRepo avroSchemaZkRepo = new SchemaZkRepo(properties);
        SchemaCacheManager schemaCacheManager = new SchemaCacheManager("mock-url");
        schemaCacheManager.setLruCache(new LruCache());
        schemaCacheManager.setZookeeperManager(zookeeperManagerMock);
        avroSchemaZkRepo.setSchemaCacheManager(schemaCacheManager);

        Object schema = avroSchemaZkRepo.getSchema("/1");
        assertTrue(schema instanceof String);
        String strSchema = (String) schema;
        assertEquals(SCHEMA, strSchema);

    }
    @Test
    public void readLatestSchema() throws RepoException{

        //mock init
        List<String> schemasList = new ArrayList<String>();
        schemasList.add("0");
        schemasList.add("0.0.1");
        schemasList.add("0.0.1-SNAPSHOT");
        schemasList.add("0.0.2");
        schemasList.add("0.0.2-SNAPSHOT");
        Mockito.when(zookeeperManagerMock.getChildrenAndDisconnect(anyString())).thenReturn(schemasList);
        String expected = "0.0.2";
        Properties properties = new Properties();
        properties.put(SchemaZkRepo.ZK_CONNECT_CONNECT, ZK_URL_VALUE);

        SchemaZkRepo avroSchemaZkRepo = new SchemaZkRepo(properties);
        SchemaCacheManager schemaCacheManager = new SchemaCacheManager("mock-url");
        schemaCacheManager.setLruCache(new LruCache());
        schemaCacheManager.setZookeeperManager(zookeeperManagerMock);
        avroSchemaZkRepo.setSchemaCacheManager(schemaCacheManager);

        String schemaId = avroSchemaZkRepo.getLatestSchemaId();
        assertEquals(expected, schemaId);
    }
    @Test
    public void readLatestSnapshotSchema() throws RepoException{

        //mock init
        List<String> schemasList = new ArrayList<String>();
        schemasList.add("0");
        schemasList.add("0.0.1");
        schemasList.add("0.0.1-SNAPSHOT");
        schemasList.add("0.0.2-SNAPSHOT");
        Mockito.when(zookeeperManagerMock.getChildrenAndDisconnect(anyString())).thenReturn(schemasList);
        String expected = "0.0.2-SNAPSHOT";
        Properties properties = new Properties();
        properties.put(SchemaZkRepo.ZK_CONNECT_CONNECT, ZK_URL_VALUE);

        SchemaZkRepo avroSchemaZkRepo = new SchemaZkRepo(properties);
        SchemaCacheManager schemaCacheManager = new SchemaCacheManager("mock-url");
        schemaCacheManager.setLruCache(new LruCache());
        schemaCacheManager.setZookeeperManager(zookeeperManagerMock);
        avroSchemaZkRepo.setSchemaCacheManager(schemaCacheManager);

        String schemaId = avroSchemaZkRepo.getLatestSchemaId();
        assertEquals(expected, schemaId);
    }

}
