package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/29/13
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */

import com.liveperson.schema.SchemaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.Random;

public abstract class AbstractConsumer implements Runnable {

    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConsumer.class);


    private Queue<byte[]> messages;
    Random random = new Random();




    public AbstractConsumer(Queue<byte[]> messages) {
        this.messages = messages;
    }

    @Override
    public void run(){

        while (true){
            try {
                Thread.sleep(random.nextInt(100) * 100);
                byte [] eventAsBytes = messages.poll();
                if (eventAsBytes != null){
                    consumerEvent(eventAsBytes);
                }

            } catch (InterruptedException e) {
                LOG.info("got InterruptedException - going out");
                break;
            }

        }

    }

    abstract public void consumerEvent(byte[] poll);



}
