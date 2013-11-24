package com.liveperson.migdalor.avro;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/10/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */



import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;

import com.liveperson.migdalor.schema.impl.AbstractDecoderImpl;
import com.liveperson.schema.SchemaRepo;
import org.apache.avro.Schema;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.IOException;


public class AvroDecoderImpl<AvroEntity extends SpecificRecordBase> extends
        AbstractDecoderImpl<DatumReader<AvroEntity> , AvroEntity > {

    private final Schema currentSchema;

    public AvroDecoderImpl(SchemaRepo<String> schemaRepo, String currentSchemaString) {
        super(schemaRepo );
        currentSchema = new Schema.Parser().parse(currentSchemaString);

    }

    @Override
    public DatumReader createEventDecoder(String schemaString) {
        Schema schema = new Schema.Parser().parse(schemaString);
        DatumReader<AvroEntity> datumReader = new SpecificDatumReader<AvroEntity>(schema,currentSchema);
        return datumReader;
    }

    @Override //tODO check multi threading usage of same reader
    public AvroEntity decodeEvent(byte[] eventBytes, DatumReader<AvroEntity> datumReader) throws MigdalorEncodingException {
        AvroEntity event = null;
        Decoder decoder = DecoderFactory.get().binaryDecoder(eventBytes, null);
        try {
            event = datumReader.read(null,decoder);
        } catch (IOException e) {
            throw new MigdalorEncodingException(e);
        }
        return event;
    }

}
