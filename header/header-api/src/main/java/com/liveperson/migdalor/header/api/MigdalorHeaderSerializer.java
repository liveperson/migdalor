package com.liveperson.migdalor.header.api;


import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/6/13
 * Time: 3:55 PM
 * @param <T> the header object
 *
 */

public interface MigdalorHeaderSerializer<T,S> extends HeaderProtocol{


    /**
     * NOTE !!!!!
     * This API have side effect it moves the pointer of the message to the start of the Event start bytes
     * and return T header
     * @param payload byteBuffer that must include migdalor header + event     *
     * @return S is the schema revision
     */
    public abstract S getRevisionAndReposition(ByteBuffer payload);

    /**
     * this API is for future implementation
     * @param event - the event to wrap
     * @param header the header object
     * @return concatenation
     */
    public abstract byte[] wrapEvent(byte[] event, T header) ;

    /**
     * this
     * @param event - the event to wrap
     * @param SchemaRevision the Schema Revision of the event
     * @return concatenation
     */
    public abstract byte[] baseWrapEvent(byte[] event, S SchemaRevision) ;

}
