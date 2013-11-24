package com.liveperson.migdalor.avro;/**
* Created with IntelliJ IDEA.
* User: ehudl
* Date: 6/10/13
* Time: 3:11 PM
*
*/



import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.migdalor.schema.impl.AbstractEncoderImpl;
import com.liveperson.schema.SchemaRepo;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroEncoderImpl<AvroEntity extends SpecificRecordBase> extends AbstractEncoderImpl<AvroEntity> {

    public AvroEncoderImpl(SchemaRepo<String> schemaRepo, String schema, String currentSchemaRevision) {
        super(schemaRepo, schema, currentSchemaRevision);
    }

    public byte[] getBytes(AvroEntity avroEvent) throws MigdalorEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<AvroEntity> writer = new SpecificDatumWriter<AvroEntity>((Class<AvroEntity>) avroEvent.getClass());
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        try {
            writer.write(avroEvent, encoder);
            encoder.flush();
            out.close();
        } catch (IOException e) {
            throw new MigdalorEncodingException(e);
        }
        return out.toByteArray();
    }

}
