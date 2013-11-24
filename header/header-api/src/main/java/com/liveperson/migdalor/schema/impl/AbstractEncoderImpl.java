package com.liveperson.migdalor.schema.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 11:08 AM
 *
 */

import com.liveperson.migdalor.header.impl.MigdalorHeaderByteSerializer;
import com.liveperson.migdalor.schema.api.SchemaEncoder;
import com.liveperson.migdalor.schema.exception.MigdalorEncodingException;
import com.liveperson.schema.RepoException;
import com.liveperson.schema.SchemaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEncoderImpl<T> implements SchemaEncoder<T>{
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEncoderImpl.class);

    private final MigdalorHeaderByteSerializer migdalorHeaderByteSerializer;

    protected final String currentSchemaRevision;

    protected final String currentSchema;

    protected final SchemaRepo<String> schemaRepo;

    protected boolean writeSchemaInRepo;


    public AbstractEncoderImpl(SchemaRepo<String> schemaRepo, String currentSchema, String currentSchemaRevision) {
        this.currentSchemaRevision = currentSchemaRevision;
        this.migdalorHeaderByteSerializer = new MigdalorHeaderByteSerializer();
        this.currentSchema = currentSchema;
        this.schemaRepo = schemaRepo;
        writeSchema();
    }

    public void writeSchema() {
        try {
            schemaRepo.addSchema(currentSchemaRevision,currentSchema);
            LOG.info("add to repository [{}] -> [{}] ",currentSchemaRevision,currentSchema);
            writeSchemaInRepo = true;
        } catch (RepoException e) {
            LOG.error(e.getMessage(),e);
            writeSchemaInRepo = false;
        }
    }


    public byte[] encode(T eventEntity) throws MigdalorEncodingException {
        try {
            byte[] eventBytes = getBytes(eventEntity);
            return migdalorHeaderByteSerializer.baseWrapEvent(eventBytes, currentSchemaRevision);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new MigdalorEncodingException("can not encode the event", e);
        }
    }


    public abstract byte[] getBytes(T eventEntity) throws MigdalorEncodingException;

    public boolean isWriteSchemaInRepo() {
        return writeSchemaInRepo;
    }
}
