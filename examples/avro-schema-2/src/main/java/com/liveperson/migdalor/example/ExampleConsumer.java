package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */

import com.liveperson.example.Event;
import com.liveperson.migdalor.avro.AvroDecoderImpl;
import com.liveperson.migdalor.schema.api.SchemaDecoder;
import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.SchemaRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Queue;


public class ExampleConsumer extends AbstractConsumer {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExampleConsumer.class);



    private SchemaDecoder<Event> decoder;


    public ExampleConsumer(Queue<byte[]> messages, SchemaRepo<String> schemaRepo) {
        super(messages);
        try {
            decoder = new AvroDecoderImpl<Event>(schemaRepo,EventUtil.schema.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void consumerEvent(byte[] eventBytes) {
        Event event = null;
        try {
            event = decoder.decode(ByteBuffer.wrap(eventBytes));
            //do something with this specific event
            LOG.info("consumer [{}] - event [{}]",EventUtil.version , event.toString());
        } catch (MigdalorEncodingException e) {
            LOG.error("consumer [{}] - got error [{}]\n\tcause [{}]",new String []{EventUtil.version ,e.getMessage(),e.getCause().toString()});
        }
    }

    public static Thread runConsumer(Queue<byte[]> concurrentLinkedQueue, SchemaRepo<String> schemaRepo) {
        ExampleConsumer consumer = new ExampleConsumer(concurrentLinkedQueue, schemaRepo);
        Thread thread = new Thread(consumer);
        thread.setDaemon(true);
        thread.start();
        return thread;
    }


}