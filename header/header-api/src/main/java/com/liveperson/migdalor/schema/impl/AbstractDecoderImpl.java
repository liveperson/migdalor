package com.liveperson.migdalor.schema.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 11:48 AM
 */

import com.liveperson.migdalor.header.impl.MigdalorHeaderByteSerializer;
import com.liveperson.migdalor.schema.api.SchemaDecoder;
import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.RepoException;
import com.liveperson.schema.SchemaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDecoderImpl<EventDecoder,EventEntity> implements SchemaDecoder<EventEntity> {


    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDecoderImpl.class);


    private final MigdalorHeaderByteSerializer migdalorHeaderByteSerializer;


    private final SchemaRepo<String> schemaRepo;


    private Map<String, EventDecoder> schemaDecoderMap;





    public AbstractDecoderImpl(SchemaRepo<String> schemaRepo) {
        migdalorHeaderByteSerializer = new MigdalorHeaderByteSerializer();
        this.schemaRepo = schemaRepo;
        schemaDecoderMap = new HashMap<String, EventDecoder>();
    }



    public EventEntity decode(ByteBuffer eventWithHeader) throws MigdalorEncodingException {
        String schemaVersion = getSchemaRevision(eventWithHeader);
        EventEntity event = null;
        try {
            byte[] eventBytes = getAllBytes(eventWithHeader);
            EventDecoder eventDecoder = getReader(schemaVersion);
            if (eventDecoder != null){
                event = decodeEvent(eventBytes, eventDecoder);
            }else{
                throw new MigdalorEncodingException(String.format("could not decode the event here is no reader for revision [%s]",schemaVersion));
            }
        } catch (Exception e) {
            throw new MigdalorEncodingException(String.format("could not decode the event from schema [%s]",schemaVersion),e);
        }
        return event;
    }


    public EventDecoder getReader(String revision) throws RepoException {
        EventDecoder eventDecoder = schemaDecoderMap.get(revision);
        if (eventDecoder == null){
            String schemaString = this.schemaRepo.getSchema(revision);
            if (schemaString != null){
                eventDecoder = createEventDecoder(schemaString);
                LOG.debug("adding reader with revision [{}] to reader cache ",revision);
                schemaDecoderMap.put(revision, eventDecoder);
            }

        }
        return eventDecoder;
    }

    public abstract EventDecoder createEventDecoder(String schemaString);

    public abstract EventEntity decodeEvent(byte[] eventBytes, EventDecoder eventDecoder) throws MigdalorEncodingException;


    protected byte [] getAllBytes(ByteBuffer remain){
        byte [] rest = new byte [remain.remaining()];
        remain.get(rest);
        return rest;
    }


    public String getSchemaRevision(ByteBuffer payload) {
        return migdalorHeaderByteSerializer.getRevisionAndReposition(payload);
    }

    public SchemaRepo<String> getSchemaRepo() {
        return schemaRepo;
    }
}
