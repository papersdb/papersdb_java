package edu.ualberta.cs.papersdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import edu.ualberta.cs.papersdb.model.publication.Publication;

@Entity
@Table(name = "PUBLISHER")
public class Publisher extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private String name;
    private Ranking ranking;
    private Set<Publication> publications = new HashSet<Publication>(0);

    @Column(name = "NAME", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "RANKING_ID")
    @Enumerated(EnumType.STRING)
    // @Type(type = "ranking")
    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    public Set<Publication> getPublications() {
        return this.publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

}
