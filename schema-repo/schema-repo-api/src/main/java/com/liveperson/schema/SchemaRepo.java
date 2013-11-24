package com.liveperson.schema;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 6/4/13
 * Time: 7:52 AM
 * A general interface for accessing general schemas
 */
public interface SchemaRepo<T> {
    /**
     *
     * @param revision
     * @param schema to be added to repo. Override existing schema if exists.
     * @throws IOException if repo throws IOException
     */
    public void addSchema(String revision, T schema) throws RepoException;

    /**
     *
     * @param revision - removes schema from repo. Does nothing if not exist.
     * @throws IOException if repo throws IOException
     */
    public void removeSchema(String revision) throws RepoException;

       /**
     *
     * @param revision
     * @return schema if exists. Null if not exists
     * @throws IOException if repo throws IOException
     */
    public T getSchema(String revision) throws RepoException;

    /**
     *
     * @return ID of latest schema in repo
     * @throws IOException if repo throws IOException
     */
    public String getLatestSchemaId() throws RepoException;
}
