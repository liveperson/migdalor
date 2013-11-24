package com.liveperson.migdalor.header.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/9/13
 * Time: 8:43 AM
 *
 */

import com.liveperson.migdalor.header.api.MigdalorHeader;
import com.liveperson.migdalor.header.api.MigdalorHeaderSerializer;

import com.liveperson.migdalor.header.dto.BaseMigdalorHeader;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestMigdalorHeaderByteSerializer {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestMigdalorHeaderByteSerializer.class);

    MigdalorHeaderSerializer<BaseMigdalorHeader,String> migdalorHeaderByteSerializer = new MigdalorHeaderByteSerializer();

    Random random = new Random();


    public void testOneEvent(){
        ByteBuffer event = createEVent();
        String schemaRevision = "1";
        byte [] eventWithHeader = migdalorHeaderByteSerializer.baseWrapEvent(event.array(),schemaRevision);
        ByteBuffer eventResultBytes = ByteBuffer.wrap(eventWithHeader);
        String result = migdalorHeaderByteSerializer.getRevisionAndReposition(eventResultBytes);
        byte[] resultBytes = getRemaining(eventResultBytes);
        Assert.assertEquals(schemaRevision,result);
        Assert.assertArrayEquals(event.array(),resultBytes);

    }


    public void testWithHeader(){
        ByteBuffer event = createEVent();
        MigdalorHeader migdalorHeader = new BaseMigdalorHeader("2");
        byte [] eventWithHeader = migdalorHeaderByteSerializer.wrapEvent(event.array(),(BaseMigdalorHeader)migdalorHeader);
        ByteBuffer eventResultBytes = ByteBuffer.wrap(eventWithHeader);
        String result = migdalorHeaderByteSerializer.getRevisionAndReposition(eventResultBytes);
        byte[] resultBytes = getRemaining(eventResultBytes);
        Assert.assertEquals("2",result);
        Assert.assertArrayEquals(event.array(),resultBytes);

    }

    private byte[] getRemaining(ByteBuffer eventResultBytes) {
        byte [] resultBytes = new byte[eventResultBytes.remaining()] ;
        eventResultBytes.get(resultBytes);
        return resultBytes;
    }

    private ByteBuffer createEVent() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BaseMigdalorHeader.INT_SIZE * 2);
        byteBuffer.putInt(random.nextInt());
        byteBuffer.putInt(random.nextInt());
        return byteBuffer;
    }

    @Test
    public void test1(){
        testOneEvent();
        testWithHeader();
    }


    @Test
    public void test2(){
        for (int i =0; i < 1000; i++){
            testOneEvent();
            testWithHeader();
        }
    }

    @Test
    public void testError(){
        ByteBuffer bytes = ByteBuffer.allocate(10);
        bytes.putInt(1);
        Assert.assertNull(migdalorHeaderByteSerializer.getRevisionAndReposition(bytes));
    }

    @Test
    public void test3() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i =0; i < 10; i++){
            executor.execute(createThread());
        }
        executor.awaitTermination(1,TimeUnit.SECONDS);
        executor.shutdownNow();
    }

    private Runnable createThread() {
        Runnable tester = new Runnable() {
            @Override
            public void run() {
                LOG.debug("t: [{}] start ",Thread.currentThread().getId());
                for (int i =0; i < 5; i++){               ;
                    testOneEvent();
                    testWithHeader();
                }
                LOG.debug("t: [{}] finish successfully ",Thread.currentThread().getId());
            }
        };
        return tester;
    }


}
