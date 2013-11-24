package com.liveperson.schema;

import com.liveperson.migdalor.curator.util.EmbeddedZK;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class ZkSchemaRepoTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZkSchemaRepoTest.class);
    public static final String SCHEMA = "1.1.1.1";

    EmbeddedZK embeddedZK;
    public static final int zkPort = 2195; // different than the default

    @Mock
    SchemaCacheManager schemaCacheManager;


    SchemaRepo schemaRepo;

    @Before
    public void before() throws RepoException {
        //mockito setup
        MockitoAnnotations.initMocks(this);
        embeddedZK = new EmbeddedZK();
        embeddedZK.setPort(zkPort);
        embeddedZK.start();

        Properties props = new Properties();
        props.setProperty(SchemaZkRepo.ZK_CONNECT_CONNECT, "localhost:" + zkPort);
        props.setProperty(SchemaZkRepo.ROOT_PATH_KEY, "/root");
        SchemaZkRepo avroSchemaRepo = new SchemaZkRepo(props);

        //stub to check schema "1"
        Mockito.when(schemaCacheManager.getSchema("/root/1")).thenReturn(SCHEMA);
        avroSchemaRepo.setSchemaCacheManager(schemaCacheManager);
        schemaRepo = avroSchemaRepo;
    }

    @After
    public void after() throws IOException {
        embeddedZK.stop();
    }


    @Test
    public void readSchemaOK() throws RepoException {

        Object schema = schemaRepo.getSchema("1");
        assertTrue(schema instanceof String);
        String strSchema = (String) schema;
        assertEquals(SCHEMA, strSchema);
    }

    @Test (expected = RepoException.class)
    public void readSchemaNotExist() throws RepoException {
        schemaRepo.getSchema("2");
    }

    @Test
    public void readLAtestSchemaIdOK() throws RepoException {
        Mockito.when(schemaCacheManager.getShemasList(anyString()))
                .thenReturn(new ArrayList<String>(Arrays.asList(SCHEMA)));

        // 1st run: retrieve list of schemas
        Object schema = schemaRepo.getLatestSchemaId();
        assertEquals(SCHEMA, schema);
    }

    @Test (expected = RepoException.class)
    public void readLAtestSchemaIdEmptyList() throws RepoException {
        Mockito.when(schemaCacheManager.getShemasList(anyString()))
                .thenReturn(new ArrayList<String>(0)); //return empty list

        // 1st run: retrieve list of schemas
        Object schema = schemaRepo.getLatestSchemaId();
        assertEquals(SCHEMA, schema);
    }

    @Test (expected = RepoException.class)
    public void readLAtestSchemaIdNull() throws RepoException {
        Mockito.when(schemaCacheManager.getShemasList(anyString()))
                .thenReturn(null);

        // 1st run: retrieve list of schemas
        Object schema = schemaRepo.getLatestSchemaId();
        assertEquals(SCHEMA, schema);
    }


}
