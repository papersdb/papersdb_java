package edu.ualberta.cs.papersdb.model.user;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ualberta.cs.papersdb.model.AbstractPapersdbModel;
import edu.ualberta.cs.papersdb.model.Paper;

@Entity
@Table(name = "USER")
public class User extends AbstractPapersdbModel {
    private static final long serialVersionUID = 1L;

    private boolean isVerified = false;
    private UserAccessType accessType = UserAccessType.UNVERIFIED_USER;
    private String password;
    private String login;
    private Date lastLogin;
    private String email;
    private String familyNames;
    private String givenNames;
    private Set<Paper> papers = new HashSet<Paper>(0);

    @NotEmpty(message = "{edu.ualberta.med.biobank.model.User.login.NotEmpty}")
    @Column(name = "LOGIN", unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "LAST_LOGIN")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "IS_VERIFIED")
    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    @Column(name = "ACCESS_TYPE")
    public UserAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(UserAccessType accessType) {
        this.accessType = accessType;
    }

    @Column(name = "EMAIL", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userSubmittedBy")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    public Set<Paper> getPapers() {
        return this.papers;
    }

    public void setPapers(Set<Paper> papers) {
        this.papers = papers;
    }

}
