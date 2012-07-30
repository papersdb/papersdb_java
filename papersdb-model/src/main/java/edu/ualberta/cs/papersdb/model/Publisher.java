package edu.ualberta.cs.papersdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private String acronym;
    private String url;
    private Ranking ranking;
    private String customRanking;
    private Set<Publication> publications = new HashSet<Publication>(0);

    @Column(name = "NAME", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "ACRONYM", unique = true, nullable = false)
    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    @Column(name = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "RANKING_ID")
    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    @Column(name = "CUSTOM_RANKING", length = 255)
    public String getCustomRanking() {
        return customRanking;
    }

    public void setCustomRanking(String customRanking) {
        this.customRanking = customRanking;
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
