package com.liveperson.migdalor.schema.api;


import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/10/13
 * Time: 3:08 PM
 *
 */
public interface SchemaEncoder<T> {

    abstract public byte [] encode(T event) throws MigdalorEncodingException;
}
