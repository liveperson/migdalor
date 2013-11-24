package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/29/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.Random;

public abstract class AbstractProducer implements Runnable{



    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConsumer.class);

    private Queue<byte []> messages;
    Random random = new Random();

    public AbstractProducer(Queue<byte[]> messages) {
        this.messages = messages;
    }


    @Override
    public void run() {
        while (true){
            try{
                sleep();
                byte [] eventAsBytes = createEvent();
                messages.add(eventAsBytes);
            }catch (InterruptedException e){
                LOG.info("got InterruptedException - going out");
                break;
            }


        }

    }

    private void sleep() throws InterruptedException {
        if (messages.size() > 0 ){
           Thread.sleep(random.nextInt(100) * 100);

        }
    }

    abstract public byte[] createEvent();
}
