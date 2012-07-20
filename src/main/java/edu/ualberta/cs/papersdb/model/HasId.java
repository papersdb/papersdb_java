package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;

public interface HasId<T extends Serializable> {
    public T getId();

    public void setId(T id);
}
