package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 5:36 PM
 */

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


    String [] types = new String [] {"type_ship", "type_boat", "type_wave", "type_send" };

    int typeLength = types.length;

    Random r = new Random();

    SchemaEncoder<Event> eventSchemaEncoder;


    public ExampleProducer(Queue<byte[]> messages, SchemaRepo<String> schemaRepo) {
        super(messages);

        this.eventSchemaEncoder = new AvroEncoderImpl<Event>(schemaRepo, EventUtil.schema.toString(), EventUtil.version);
    }



    @Override
    public byte[] createEvent() {
        String type = types[r.nextInt(typeLength)];
        Event event = new Event(EventUtil.version, System.currentTimeMillis(),"hi there ! I removed the color field and added type","type_2");
        //do something with the event of version 2
        LOG.info("producer version [{}] added event with type [{}]",EventUtil.version , type);
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
