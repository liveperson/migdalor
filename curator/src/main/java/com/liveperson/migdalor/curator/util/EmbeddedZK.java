package com.liveperson.migdalor.curator.util;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/30/13
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.zookeeper.server.NIOServerCnxn;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Scanner;

public class EmbeddedZK {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedZK.class);

    public static final int TICK_TIME = 3000;

    public static final int DEFAULT_CLIENT_PORT = 2181;
    public static final int DEFAULT_NUM_OF_CONNECTION = 100;


    private int tickTime;

    private File dataDir;
    private static final String DEFAULT_ZK_DATA_DIR = "/tmp/embedded/zk";


    private NIOServerCnxnFactory standaloneServerFactory;
    private ZooKeeperServer server;

    private int maxSessionTimeout;
    private int minSessionTimeout;
    private int numOfConnection;
    private int port;

    public static EmbeddedZK createWithPort(int port){
        EmbeddedZK zk  = new EmbeddedZK();
        zk.setPort(port);
        return zk;
    }

    public EmbeddedZK() {
        this(TICK_TIME, new File(DEFAULT_ZK_DATA_DIR), 100000,10000, DEFAULT_NUM_OF_CONNECTION, DEFAULT_CLIENT_PORT);

    }

    public EmbeddedZK(int tickTime, File dataDir, int maxSessionTimeout, int minSessionTimeout, int numOfConnection, int port) {
        this.tickTime = tickTime;
        this.dataDir = dataDir;

        this.maxSessionTimeout = maxSessionTimeout;
        this.minSessionTimeout = minSessionTimeout;
        this.numOfConnection = numOfConnection;
        this.port = port;
    }

    public void start(){
        try{
            server = new ZooKeeperServer(dataDir, dataDir, tickTime);
            server.setMaxSessionTimeout(maxSessionTimeout);
            server.setMinSessionTimeout(minSessionTimeout);

            standaloneServerFactory = new NIOServerCnxnFactory();
            standaloneServerFactory.configure(new InetSocketAddress(port), numOfConnection);

            standaloneServerFactory.startup(server); // start the server.
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void stop(){

        if (standaloneServerFactory != null){
            standaloneServerFactory.shutdown();
        }else{
            LOG.error("can not shut down the standaloneServerFactory is null");
        }
        if (server != null){
            server.shutdown();

        }else{
            LOG.error("can not shut down the zk server is null");
        }
    }


    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public void setMaxSessionTimeout(int maxSessionTimeout) {
        this.maxSessionTimeout = maxSessionTimeout;
    }

    public void setMinSessionTimeout(int minSessionTimeout) {
        this.minSessionTimeout = minSessionTimeout;
    }

    public void setNumOfConnection(int numOfConnection) {
        this.numOfConnection = numOfConnection;
    }

    public void setPort(int port) {
        this.port = port;
    }

//    public static void main(String[] args) throws InterruptedException {
//        EmbeddedZK zkLocalServer = new EmbeddedZK();
//
//        zkLocalServer.start();
//
//        System.out.println("zk  is up");
//
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()){
//            if (scanner.nextLine().contains("exit")){
//                break;
//            }
//        }
//
//        System.out.println("zk and kafak is going down");
//
//        zkLocalServer.stop();
//
//    }

}
