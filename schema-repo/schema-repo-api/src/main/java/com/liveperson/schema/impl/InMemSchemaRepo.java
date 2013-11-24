package com.liveperson.schema.impl;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/23/13
 * Time: 12:07 PM
 */

import com.liveperson.schema.BadSchemaException;
import com.liveperson.schema.RepoException;
import com.liveperson.schema.SchemaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class InMemSchemaRepo<T>  implements SchemaRepo<T>{

    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(InMemSchemaRepo.class);


    private final Map<String,T> repo = new HashMap<String, T>();

    @Override
    public void addSchema(String revision, T schema) throws RepoException {
        repo.put(revision,schema);
    }

    @Override
    public void removeSchema(String revision) throws RepoException {
        repo.remove(revision);
    }


    @Override
    public T getSchema(String revision) throws RepoException {
        T schema =  repo.get(revision);
        if (schema == null){
            throw new RepoException(String.format("no schema for revision [%s]",revision));
        }
        return schema;

    }

    @Override
    public String getLatestSchemaId() throws RepoException {
        throw new NotImplementedException();
    }
}
