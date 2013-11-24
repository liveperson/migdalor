/*
* LivePerson copyrights will be here...
*/
package com.liveperson.migdalor.schema.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author yaarr
 * @version 0.0.1
 * @since 7/17/13, 17:36
 */
public class MigdalorEncodingExceptionTest {
    private static final Logger LOG = LoggerFactory.getLogger(MigdalorEncodingExceptionTest.class);

    @Test(expected = MigdalorEncodingException.class)
    public void testThrow1() throws MigdalorEncodingException {
       throw new MigdalorEncodingException(new IOException());
    }

    @Test(expected = MigdalorEncodingException.class)
    public void testThrow2() throws MigdalorEncodingException {
       throw new MigdalorEncodingException("just a test",new IOException());
    }

}
