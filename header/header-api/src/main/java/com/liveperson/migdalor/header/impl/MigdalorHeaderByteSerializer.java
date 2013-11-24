package com.liveperson.migdalor.header.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/6/13
 * Time: 4:03 PM
 */

import com.liveperson.migdalor.header.api.MigdalorHeader;
import com.liveperson.migdalor.header.api.MigdalorHeaderSerializer;

import com.liveperson.migdalor.header.dto.BaseMigdalorHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class MigdalorHeaderByteSerializer implements MigdalorHeaderSerializer<BaseMigdalorHeader,String> {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MigdalorHeaderByteSerializer.class);

    @Override
    public String getRevisionAndReposition(ByteBuffer payload) {
        int version = payload.getInt();
        if (version == PROTOCOL_VERSION){
            int schemaVersionSize = payload.getInt();
            byte [] stringSchema = new  byte [schemaVersionSize];
            payload.get(stringSchema);
            String schemaVersion = new String(stringSchema);
            return schemaVersion;
        }else{
            LOG.error("protocol version is not the same don't know what to do");
            return null;
        }
    }

    @Override
    public byte[] wrapEvent(byte[] event, BaseMigdalorHeader header) {
        return wrapEvent(event,header.getSchemaVersion(), header.getRevisionLengthInBytes());
    }

    /**
     *
     * @param event - the event to wrap
     * @param schemaRevision
     * * @return
     */
    @Override
    public byte[] baseWrapEvent(byte[] event, String schemaRevision) {
        byte [] schemaRevisionBytes = schemaRevision.getBytes();
        byte [] schemaRevisionBytesLength = ByteBuffer.allocate(MigdalorHeader.INT_SIZE).putInt(schemaRevision.length()).array();
        return wrapEvent(event,schemaRevisionBytes, schemaRevisionBytesLength);
    }

    private byte[] wrapEvent(byte[] event, byte [] schemaRevisionBytes, byte [] schemaRevisionBytesLength){
        int size = MigdalorHeader.INT_SIZE * 2 + schemaRevisionBytes.length + event.length;
        ByteBuffer messageWrapper = ByteBuffer.allocate(size);
        messageWrapper.put(MigdalorHeader.PROTOCOL_VERSION_BYTES);
        messageWrapper.put(schemaRevisionBytesLength);
        messageWrapper.put(schemaRevisionBytes);
        messageWrapper.put(event);
        return messageWrapper.array();
    }


}
