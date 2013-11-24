package com.liveperson.migdalor.header.api;

import java.nio.ByteBuffer;

/**




/* Created with IntelliJ IDEA.
        * User: ehudl
        * Date: 6/9/13
        * Time: 8:54 AM
        *
 * @param <S> the schema revision
 */
public interface MigdalorHeader<S> extends HeaderProtocol {

    public static final int INT_SIZE = 4;

    public final byte[] PROTOCOL_VERSION_BYTES = ByteBuffer.allocate(INT_SIZE).putInt(PROTOCOL_VERSION).array();

    public S getSchemaRevision();
}
