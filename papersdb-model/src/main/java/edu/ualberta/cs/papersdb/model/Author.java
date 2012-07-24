package edu.ualberta.cs.papersdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "AUTHOR")
public class Author extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private String title;
    private String familyNames;
    private String givenNames;
    private String email;
    private Set<AuthorRanked> papers = new HashSet<AuthorRanked>(0);

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotEmpty(message = "{edu.ualberta.cs.papersDb.model.Author.familyNames.NotEmpty}")
    @Column(name = "FAMILY_NAMES", nullable = false)
    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(String familyNames) {
        this.familyNames = familyNames;
    }

    @NotEmpty(message = "{edu.ualberta.cs.papersDb.model.Author.givenNames.NotEmpty}")
    @Column(name = "GIVEN_NAMES", nullable = false)
    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    @Column(name = "EMAIL", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<AuthorRanked> getPapers() {
        return papers;
    }

    public void setPapers(Set<AuthorRanked> papers) {
        this.papers = papers;
    }

}
