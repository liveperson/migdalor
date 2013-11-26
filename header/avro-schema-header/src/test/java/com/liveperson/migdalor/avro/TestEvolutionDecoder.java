package com.liveperson.migdalor.avro;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/11/13
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */

import com.liveperson.example.Header;
import com.liveperson.example.TestExample;
import com.liveperson.migdalor.header.impl.MigdalorHeaderByteSerializer;
import com.liveperson.migdalor.schema.api.SchemaDecoder;
import com.liveperson.migdalor.schema.api.SchemaEncoder;
import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.RepoException;
import com.liveperson.schema.SchemaRepo;
import com.liveperson.schema.impl.InMemSchemaRepo;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class TestEvolutionDecoder {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestEvolutionDecoder.class);
    public static final String HEADER = "header";
    public static final String BODY = "body";

    private MigdalorHeaderByteSerializer migdalorHeaderByteSerializer = new MigdalorHeaderByteSerializer();

    private static final String runTimeField = "runTimeField";

    private static final String runTimeFieldSchema = "{\"type\":\"string\",\"name\":\"%s\",\"default\":\"\"}";

    private SchemaEncoder<TestExample> schemaEncoder;

    private SchemaDecoder<TestExample> schemaDecoder;

    private SchemaRepo<String> schemaRepo;



    public final static String version = "1.0";


    private Schema schema;

    @Before
    public void setUp() throws Exception {
        TestExample event = TestExample.class.newInstance();
        event.setHeader(new Header());
        schema = event.getSchema();
        schemaRepo = new InMemSchemaRepo<String>();
        schemaEncoder = new AvroEncoderImpl<TestExample>(schemaRepo,schema.toString(), version);
        schemaDecoder = new AvroDecoderImpl<TestExample>(schemaRepo,schema.toString());

    }

    @Test(expected = MigdalorEncodingException.class)
    public void testNullRevision() throws MigdalorEncodingException {
        byte[] encodedWithHeader = migdalorHeaderByteSerializer.baseWrapEvent(new byte[0], "notExistRevision");
        TestExample event = schemaDecoder.decode(ByteBuffer.wrap(encodedWithHeader));

    }


    @Test
    public void testAddFieldCompatible() throws IOException, MigdalorEncodingException, RepoException {
        String newSchemaRevision = version + "_testBackwardCompatible";
        List<Schema.Field> newFields = createNewFields(schema, createRuntimeStringField(runTimeField));
        Schema myNewSchema = createNewSchema(schema, newFields);
        schemaRepo.addSchema(newSchemaRevision, myNewSchema.toString());
        GenericRecord datum = createDatum(myNewSchema);
        LOG.debug("original event [{}]", datum);
        byte[] encodedWithHeader = migdalorHeaderByteSerializer.baseWrapEvent(datumToBytes(myNewSchema, datum), newSchemaRevision);
        TestExample event = schemaDecoder.decode(ByteBuffer.wrap(encodedWithHeader));
        LOG.debug("event after decoding [{}]", event);

        Header originalHeader = (Header)datum.get(HEADER);
        Assert.assertEquals(originalHeader.getType().toString(),event.getHeader().getType().toString());
        Assert.assertEquals(originalHeader.getTime(),event.getHeader().getTime());
        Assert.assertEquals(datum.get(BODY).toString(),event.getBody().toString());
    }

    @Test(expected = MigdalorEncodingException.class)
    public void testAddFieldCompatibleBadSchema() throws IOException, RepoException, MigdalorEncodingException {
        String newSchemaRevision = version + "_testBackwardCompatible";
        List<Schema.Field> newFields = createNewFields(schema, createRuntimeStringField(runTimeField));
        Schema myNewSchema = createNewSchema(schema, newFields);

        //here i put the original schema instead the new one
        schemaRepo.addSchema(newSchemaRevision, schema.toString());

        GenericRecord datum = createDatum(myNewSchema);
        LOG.debug("original event [{}]", datum);
        byte[] encodedWithHeader = migdalorHeaderByteSerializer.baseWrapEvent(datumToBytes(myNewSchema, datum), newSchemaRevision);
        //should return exception
       schemaDecoder.decode(ByteBuffer.wrap(encodedWithHeader));

    }


    @Test
    public void testRemoveField() throws IOException, MigdalorEncodingException, RepoException {
        String newSchemaRevision = version + "_testForwardCompatible";
        List<Schema.Field> newFields = createNewFields(schema, null);
        removeField(newFields, BODY);
        Schema myNewSchema = createNewSchema(schema, newFields);
        schemaRepo.addSchema(newSchemaRevision, myNewSchema.toString());
        GenericRecord datum = createDatumNoBody(myNewSchema);
        LOG.debug("original event [{}]", datum);

        MigdalorHeaderByteSerializer migdalorHeaderByteSerializer = new MigdalorHeaderByteSerializer();
        byte[] encodedWithHeader = migdalorHeaderByteSerializer.baseWrapEvent(datumToBytes(myNewSchema, datum), newSchemaRevision);
        TestExample event = schemaDecoder.decode(ByteBuffer.wrap(encodedWithHeader));
        LOG.debug("event after decoding [{}]", event);

        Header original = (Header)datum.get("header");
        Assert.assertEquals(original.getType().toString(),event.getHeader().getType().toString());
        Assert.assertEquals(original.getTime(),event.getHeader().getTime());
        Assert.assertEquals("",event.getBody().toString());

    }

    private List<Schema.Field> removeField(List<Schema.Field> fields, String fieldName) {
        int j = getIndexOf(fields, fieldName);
        if (j != -1) {
            fields.remove(j);
        } else {
            LOG.error("can not remove field [{}] it is not exist", fieldName);
        }
        return fields;

    }

    private int getIndexOf(List<Schema.Field> fields, String fieldName) {
        int j = -1;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).name().equals(fieldName)) {
                j = i;
                break;
            }
        }
        return j;
    }

    private byte[] datumToBytes(Schema myNewSchema, GenericRecord datum) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder e = EncoderFactory.get().binaryEncoder(outputStream, null);
        GenericDatumWriter w = new GenericDatumWriter(myNewSchema);
        w.write(datum, e);
        e.flush();
        return outputStream.toByteArray();
    }

    private GenericRecord createDatum(Schema myNewSchema) {
        GenericRecord datum = createDatumNoBody(myNewSchema);
        datum.put(runTimeField, "my run time field ");
        datum.put(BODY, "this is my body message");
        return datum;
    }

    private GenericRecord createDatumNoBody(Schema myNewSchema) {
        GenericRecord datum = new GenericData.Record(myNewSchema);
        Header commonHeader = new Header();
        commonHeader.setType("TEST_TYPE");
        commonHeader.setTime(System.currentTimeMillis());
        datum.put(HEADER, commonHeader);
        return datum;
    }

    private Schema createNewSchema(Schema schema, List<Schema.Field> newFields) {
        Schema myNewSchema = Schema.createRecord(schema.getName(), schema.getDoc(), schema.getNamespace(), schema.isError());
        myNewSchema.setFields(newFields);
        return myNewSchema;
    }

    private List<Schema.Field> createNewFields(Schema originalSchema, Schema.Field field) {
        List<Schema.Field> oldFields = originalSchema.getFields();
        List<Schema.Field> newFields = new LinkedList<Schema.Field>();
        if (field != null){
            newFields.add(field);
        }
        for (Schema.Field curre : oldFields) {
            newFields.add(new Schema.Field(curre.name(), curre.schema(), curre.doc(), curre.defaultValue()));
        }
        return newFields;
    }

    public static Schema.Field createRuntimeStringField(String field) throws IOException {
        Schema runTimeFieldSchemaObj = new Schema.Parser().parse(String.format(runTimeFieldSchema, field));
        return new Schema.Field(field, runTimeFieldSchemaObj, "doc", create());
    }

    @SuppressWarnings("no idea what this is but it is working !!")
    private static JsonNode create() throws IOException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = factory.createJsonParser("{\"k1\":\"v1\"}");
        JsonNode actualObj = mapper.readTree(jp);
        return actualObj;
    }
}
