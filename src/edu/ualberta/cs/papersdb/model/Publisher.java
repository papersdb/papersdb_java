package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PUBLISHER")
public class Publisher implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Ranking ranking;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    @ElementCollection
    @CollectionTable(name = "PUBLICATION_RANKING", joinColumns = @JoinColumn(name = "PUBLICATION_ID"))
    @Column(name = "RANKING_ID")
    @Type(type = "ranking")
    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

}
