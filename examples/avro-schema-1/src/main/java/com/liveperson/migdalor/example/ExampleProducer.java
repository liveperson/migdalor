package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 5:36 PM
 */

import com.liveperson.example.Color;
import com.liveperson.example.Event;

import com.liveperson.migdalor.avro.AvroEncoderImpl;
import com.liveperson.migdalor.schema.api.SchemaEncoder;
import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.SchemaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.Random;


public class ExampleProducer extends AbstractProducer {

    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExampleProducer.class);


    SchemaEncoder<Event> eventSchemaEncoder;

    int colorPptions = Color.values().length;

    Random random = new Random();

    public ExampleProducer(Queue<byte[]> messages, SchemaRepo<String> schemaRepo) {
        super(messages);
        this.eventSchemaEncoder = new AvroEncoderImpl<Event>(schemaRepo, EventUtil.schema.toString(), EventUtil.version);
    }



    @Override
    public byte[] createEvent() {
        Color color = Color.values()[random.nextInt(colorPptions)];
        Event event = new Event(EventUtil.version, System.currentTimeMillis(),"hi there !", color);
        LOG.info("producer version [{}] created event and choosed color [{}]",EventUtil.version,color);
        //do something with the event version 1
        try {
            return eventSchemaEncoder.encode(event);
        } catch (MigdalorEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static Thread runProducer(Queue<byte[]> messages, SchemaRepo<String> schemaRepo){
        ExampleProducer exampleProducer = new ExampleProducer(messages, schemaRepo);
        Thread thread = new Thread(exampleProducer);
        thread.setDaemon(true);
        thread.start();
        return thread;
    }


}

