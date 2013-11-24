package com.liveperson.migdalor.schema.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 2:50 PM
 */

import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.SchemaRepo;
import com.liveperson.schema.impl.InMemSchemaRepo;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TestAbstractDecoderEncoder {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestAbstractDecoderEncoder.class);

    private SchemaRepo<String> schemaRepo;

    @Before
    public void setUp() throws Exception {
        schemaRepo = new InMemSchemaRepo<String>();

    }

    @Test
    public void testWriteReadFromRepo() throws Exception {
        String schema = "TEST";
        MyEncoder encoder = new MyEncoder(schemaRepo,schema,"1");
        String event = "event";
        byte[] bytes = encoder.encode(event);

        AbstractDecoderImpl<MyDecoder.StringDecoder,String> decoder = new MyDecoder(schemaRepo);
        String actual = decoder.decode(ByteBuffer.wrap(bytes));
        Assert.assertEquals(event+schema , actual);



    }

    public class MyEncoder extends AbstractEncoderImpl<String>{

        public MyEncoder(SchemaRepo<String> schemaRepo, String currentSchema, String currentSchemaRevision) {
            super(schemaRepo, currentSchema, currentSchemaRevision);
        }

        @Override
        public byte[] getBytes(String eventEntity) throws MigdalorEncodingException {
            return eventEntity.getBytes();
        }
    }


    public class MyDecoder extends AbstractDecoderImpl<MyDecoder.StringDecoder,String>{




        public MyDecoder(SchemaRepo<String> schemaRepo) {
            super(schemaRepo);

        }

        @Override
        public StringDecoder createEventDecoder(String schemaString) {
            return new StringDecoder(schemaString);
        }

        @Override
        public String decodeEvent(byte[] eventBytes, StringDecoder stringDecoder) throws MigdalorEncodingException {
            return new String(eventBytes)+stringDecoder.schema;
        }

        public class StringDecoder{
            String schema;
            private StringDecoder(String schema) {
                this.schema = schema;
            }


        }

    }
}
