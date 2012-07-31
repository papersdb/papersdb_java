package edu.ualberta.cs.papersdb.model.publication;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Thesis")
public class ThesisPub extends Publication {
    private static final long serialVersionUID = 1L;

    private String intitution;
    private String thesisType;

    @Column(name = "INSTITUTION")
    public String getIntitution() {
        return intitution;
    }

    public void setIntitution(String intitution) {
        this.intitution = intitution;
    }

    @Column(name = "THESIS_TYPE")
    public String getThesisType() {
        return thesisType;
    }

    public void setThesisType(String thesisType) {
        this.thesisType = thesisType;
    }
}
