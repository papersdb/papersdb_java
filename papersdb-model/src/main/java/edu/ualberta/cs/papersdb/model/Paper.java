package edu.ualberta.cs.papersdb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Null;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ualberta.cs.papersdb.model.publication.Publication;
import edu.ualberta.cs.papersdb.model.user.User;

@Entity
@Table(name = "PAPER",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "TITLE", "PUBLICATION_ID" }) })
public class Paper extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private String title;
    private String pubAbstract;
    private String keywords;
    private String extraInformation;
    private Date paperDate;
    private Date dbInsertDate;
    private Date dbUpdateDate;
    private User userSubmittedBy;
    private String userTags;
    private Ranking ranking;
    private String customRanking;
    private String doi;
    private Set<Collaboration> collaborations = new HashSet<Collaboration>(0);
    private boolean isPublic = false;
    private SortedSet<AuthorRanked> authors = new TreeSet<AuthorRanked>();
    private Publication publication;
    private Set<Paper> relatedPapers = new HashSet<Paper>(0);
    private Set<String> relatedUrls = new HashSet<String>(0);
    private Set<String> attachments = new HashSet<String>(0);

    @NotEmpty(message = "{edu.ualberta.cs.papersDb.model.Paper.title.NotEmpty}")
    @Column(name = "TITLE", length = 500, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "ABSTRACT", columnDefinition = "TEXT")
    public String getAbstract() {
        return pubAbstract;
    }

    public void setAbstract(String pubAbstract) {
        this.pubAbstract = pubAbstract;
    }

    @Column(name = "KEYWORDS", length = 255)
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Column(name = "EXTRA_INFORMATION", columnDefinition = "TEXT")
    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "PAPER_DATE")
    public Date getPaperDate() {
        return paperDate;
    }

    public void setPaperDate(Date date) {
        this.paperDate = date;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DB_INSERT_DATE")
    public Date getDbInsertDate() {
        return dbInsertDate;
    }

    public void setDbInsertDate(Date insertDate) {
        this.dbInsertDate = insertDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DB_UPDATE_DATE")
    public Date getDbUpdateDate() {
        return dbUpdateDate;
    }

    public void setDbUpdateDate(Date updateDate) {
        this.dbUpdateDate = updateDate;
    }

    @Null(message = "{edu.ualberta.cs.papersdb.model.Paper.userSubmittedBy.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBMITTED_BY_USER_ID")
    public User getUserSubmittedBy() {
        return userSubmittedBy;
    }

    public void setUserSubmittedBy(User user) {
        this.userSubmittedBy = user;
    }

    @Column(name = "USER_TAGS", length = 255)
    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
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

    /*
     * Papers should be allowed to be saved with a null DOI.
     */
    @Column(name = "DOI", unique = true)
    public String getDOI() {
        return doi;
    }

    public void setDOI(String doi) {
        this.doi = doi;
    }

    @ElementCollection(targetClass = Collaboration.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "PAPER_COLLABORATION", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "COLLABORATION_ID", nullable = false)
    @Enumerated(EnumType.STRING)
    // @Type(type = "collaboration")
    public Set<Collaboration> getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
    }

    @Column(name = "PUBLIC")
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RANK")
    @Sort(type = SortType.COMPARATOR, comparator = AuthorRankedComparator.class)
    public SortedSet<AuthorRanked> getAuthors() {
        return authors;
    }

    public void setAuthors(SortedSet<AuthorRanked> authors) {
        this.authors = authors;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @ForeignKey(name = "FK_PAPER_PUBLICATION")
    @JoinColumn(name = "PUBLICATION_ID", nullable = true)
    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PAPER_PAPER", joinColumns = { @JoinColumn(name = "FROM_PAPER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "TO_PAPER_ID", nullable = false, updatable = false) })
    public Set<Paper> getRelatedPapers() {
        return relatedPapers;
    }

    public void setRelatedPapers(Set<Paper> relatedPapers) {
        this.relatedPapers = relatedPapers;
    }

    @ElementCollection
    @CollectionTable(name = "PAPER_URL", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "URL")
    public Set<String> getRelatedUrls() {
        return relatedUrls;
    }

    public void setRelatedUrls(Set<String> relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    @ElementCollection
    @CollectionTable(name = "PAPER_ATTACHMENT", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "ATTACHMENT")
    public Set<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }

}
