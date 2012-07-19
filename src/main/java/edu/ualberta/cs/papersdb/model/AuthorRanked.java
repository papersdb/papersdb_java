package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "AUTHOR_RANKED")
public class AuthorRanked implements Serializable, Comparable<AuthorRanked> {
    private static final long serialVersionUID = 1L;

    private Long id;
    private int rank;
    private Author author;
    private Paper paper;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "RANK")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @NotNull(message = "{edu.ualberta.cs.papersDb.model.AuthorRanked.author.NotEmpty}")
    @OneToOne(cascade = CascadeType.ALL)
    @ForeignKey(name = "FK_AUTHOR_RANKED_AUTHOR")
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public int compareTo(AuthorRanked ar) {
        return rank - ar.rank;
    }

    @NotNull(message = "{edu.ualberta.cs.papersDb.model.AuthorRanked.paper.NotEmpty}")
    @ManyToOne
    @ForeignKey(name = "FK_AUTHOR_RANKED_PAPER")
    @JoinColumn(name = "PAPER_ID")
    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

}
