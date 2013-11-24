package com.liveperson.migdalor.header.dto;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 6/6/13
 * Time: 6:02 PM
 */

import com.liveperson.migdalor.header.api.MigdalorHeader;


import java.nio.ByteBuffer;

public class BaseMigdalorHeader implements MigdalorHeader<String> {



    private final byte [] schemaVersion;

    private final byte[] revisionLengthInBytes;

    private final String schemaRevision;


    public BaseMigdalorHeader(String schemaRevision){
        schemaVersion = schemaRevision.getBytes();
        revisionLengthInBytes = ByteBuffer.allocate(INT_SIZE).putInt(schemaVersion.length).array();
        this.schemaRevision = schemaRevision;
    }




    public byte[] getSchemaVersion() {
        return schemaVersion;
    }

    public byte[] getRevisionLengthInBytes() {
        return revisionLengthInBytes;
    }

    @Override
    public String getSchemaRevision() {
        return schemaRevision;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseMigdalorHeader that = (BaseMigdalorHeader) o;

        if (schemaRevision != null ? !schemaRevision.equals(that.schemaRevision) : that.schemaRevision != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return schemaRevision != null ? schemaRevision.hashCode() : 0;
    }
}
