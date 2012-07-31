package edu.ualberta.cs.papersdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PUBLISHER")
public class Publisher extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private String name;
    private String acronym;
    private String url;
    private Ranking ranking;
    private String customRanking;

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

}
