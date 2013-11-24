/*
* LivePerson copyrights will be here...
*/
package com.liveperson.migdalor.header.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author yaarr
 * @version 0.0.1
 * @since 7/17/13, 17:18
 */
public class BaseMigdalorHeaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseMigdalorHeaderTest.class);

    BaseMigdalorHeader basemigdalorHeader;

    @Before
    public void setUp() throws Exception {
        basemigdalorHeader = new BaseMigdalorHeader("1");
    }

    @Test
    public void testGetSchemaVersion() throws Exception {
        Assert.assertArrayEquals("1".getBytes(), basemigdalorHeader.getSchemaVersion());
    }

    @Test
    public void testGetRevisionLengthInBytes() throws Exception {
        Assert.assertArrayEquals(ByteBuffer.allocate(4).putInt(1).array(),basemigdalorHeader.getRevisionLengthInBytes());
    }

    @Test
    public void testGetSchemaRevision() throws Exception {
        Assert.assertEquals("1", basemigdalorHeader.getSchemaRevision());
    }

    @Test
    public void testEquals() throws Exception {
        BaseMigdalorHeader thatEquals = new BaseMigdalorHeader("1");
        BaseMigdalorHeader thatNotEquals = new BaseMigdalorHeader("2");
        Assert.assertEquals(thatEquals, basemigdalorHeader);
        Assert.assertNotSame(thatNotEquals, basemigdalorHeader);

    }

    @Test
    public void testHashCode() throws Exception {
        BaseMigdalorHeader badHeader = new BaseMigdalorHeader("");
        Assert.assertEquals("1".hashCode(),basemigdalorHeader.hashCode());
        Assert.assertEquals(0,badHeader.hashCode());
    }
}
