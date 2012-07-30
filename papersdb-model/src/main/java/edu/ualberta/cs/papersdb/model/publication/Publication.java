package edu.ualberta.cs.papersdb.model.publication;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import edu.ualberta.cs.papersdb.model.AbstractPapersdbModel;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Publisher;

@Entity
@Table(name = "PUBLICATION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR",
    discriminatorType = DiscriminatorType.STRING)
public abstract class Publication extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private String name;
    private Date publishedDate;
    private Publisher publisher;
    private Paper paper;

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.DATE)
    @NotNull(message = "{edu.ualberta.cs.papersDb.model.Publication.publishedDate.NotEmpty}")
    @Column(name = "PUBLISHED_DATE", nullable = false)
    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date date) {
        this.publishedDate = date;
    }

    @NotNull(message = "{edu.ualberta.cs.papersDb.model.Publication.publisher.NotEmpty}")
    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "FK_PUBLICATION_PUBLISHER")
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "publication", orphanRemoval = true)
    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

}
