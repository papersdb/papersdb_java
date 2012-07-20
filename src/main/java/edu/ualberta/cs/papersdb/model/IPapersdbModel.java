package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;

public interface IPapersdbModel extends Serializable, HasId<Long> {
    @Override
    public Long getId();

    @Override
    public void setId(Long id);
}
