package edu.ualberta.cs.papersdb.server.dao;

import java.io.Serializable;
import java.util.Set;

public interface GenericDAO<T, ID extends Serializable> {

    T getById(ID id);

    Set<T> getAll();

    Set<T> getByExample(T exampleInstance, String... excludeProperties);

    T save(T entity);

    void delete(T entity);

    void flush();

    void clear();

}