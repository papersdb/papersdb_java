package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "AUTHOR")
public class Author implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String familyNames;
    private String givenNames;
    private String email;
    private Set<Paper> papers;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "FAMILY_NAMES")
    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(String familyNames) {
        this.familyNames = familyNames;
    }

    @Column(name = "GIVEN_NAMES")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTHOR_PAPER",
        joinColumns = { @JoinColumn(name = "AUTHOR_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "PAPER_ID", nullable = false, updatable = false) })
    public Set<Paper> getPubEntries() {
        return papers;
    }

    public void setPapers(Set<Paper> pubEntries) {
        this.papers = pubEntries;
    }

}
