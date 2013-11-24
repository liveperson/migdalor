package com.liveperson.migdalor.schema.exception;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 7/2/13
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */


import java.io.IOException;

public class MigdalorEncodingException extends Exception {

    public MigdalorEncodingException(Exception e) {
        super(e);
    }

    public MigdalorEncodingException(String message, Exception e) {
        super(message, e);
    }

    public MigdalorEncodingException(String message){
        super(message);
    }
}
