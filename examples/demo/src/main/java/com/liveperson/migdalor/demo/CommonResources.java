package com.liveperson.migdalor.demo;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/30/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

import com.liveperson.migdalor.curator.util.EmbeddedZK;
import com.liveperson.schema.SchemaZkRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommonResources {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(CommonResources.class);


    public static final String BACK = "../";
    public static final String AVRO_SCHEMA = "avro-schema";
    public static final String SHADED_JAR = "-shaded.jar";
    public static final String VERSION = "-0.0.0.1-SNAPSHOT";
    public static final String TARGET = "/target/";
    public static final String EXAMPLE_PRODUCER = "com.liveperson.migdalor.example.ExampleProducer";
    public static final String EXAMPLE_CONSUMER = "com.liveperson.migdalor.example.ExampleConsumer";
    public static final String RUN_PRODUCER = "runProducer";
    public static final String RUN_CONSUMER = "runConsumer";
    public static final int numberOfThreads = 3;
    public static final int zkPort = 2183; // different than the default

    final Properties zkSchemaRepoProperties;

    final Queue<byte []> concurrentLinkedQueue;


    EmbeddedZK embeddedZK;


    public CommonResources() {
        embeddedZK = new EmbeddedZK();
        embeddedZK.setPort(zkPort);

        zkSchemaRepoProperties = new Properties();
        zkSchemaRepoProperties.setProperty(SchemaZkRepo.ZK_CONNECT_CONNECT, "localhost:" + zkPort);
        concurrentLinkedQueue = new ConcurrentLinkedQueue<byte []>();
    }

    public void startZk(){
        embeddedZK.start();
    }

    public void stopZK(){
        embeddedZK.stop();
        LOG.info("stopping the ZK");
    }

    public static String [] getProducerConsumerJars(){
        String path = Demo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //in case of jar running
        String examplesRoot = path + BACK+ BACK +BACK;
        if (path.contains("demo"+VERSION+"-shaded.jar")){
            path = path.replaceAll("demo"+VERSION+"-shaded.jar","");
            examplesRoot = path + BACK+ BACK;
        }

        String [] arr = new String [] {
                examplesRoot + AVRO_SCHEMA + "-1" + TARGET + AVRO_SCHEMA + "-1" + VERSION + SHADED_JAR,
                examplesRoot + AVRO_SCHEMA + "-2" + TARGET + AVRO_SCHEMA + "-2" + VERSION + SHADED_JAR
        };
        return arr;
    }



}
