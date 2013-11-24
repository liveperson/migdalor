package com.liveperson.migdalor.schema.api;


import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;


import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/10/13
 * Time: 3:09 PM
 *
 */
public interface SchemaDecoder<T> {

    abstract public T decode(ByteBuffer eventWithHeader) throws MigdalorEncodingException;
}
