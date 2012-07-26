package edu.ualberta.cs.papersdb.persist.dao.hibernate;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.persist.dao.AuthorDAO;

@Repository
public class AuthorDAOHibernateImpl
    extends GenericHibernateDAO<Author, Long>
    implements AuthorDAO {

    private static final Logger log = LoggerFactory.getLogger(
        PaperDAOHibernateImpl.class);

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Author getByFamilyName(String familyName) {
        log.debug("getByFamilyName: {}", familyName);

        Author author = new Author();
        author.setFamilyNames(familyName);
        Set<Author> result = getByExample(author);
        if (result.size() > 1) {
            throw new IllegalStateException(MessageFormat
                .format("more than one author with family name: \"{0}\"",
                    familyName));
        }
        return result.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Author> getMatchingFamilyName(String match, int start, int max) {
        log.debug("getMatchingFamilyName: match={} start={} max={}",
            new Object[] { match, start, max });

        Criteria crit = getSession().createCriteria(Author.class);
        crit.add(Restrictions.ilike("familyNames", match, MatchMode.ANYWHERE));
        crit.addOrder(Order.asc("familyNames"));
        crit.addOrder(Order.asc("givenNames"));
        crit.setFirstResult(start);
        crit.setMaxResults(max);
        return new HashSet<Author>(crit.list());
    }

    @Override
    public Author getByEmail(String email) {
        log.debug("getByEmail: {}", email);

        Author author = new Author();
        author.setEmail(email);
        Set<Author> result = getByExample(author);
        if (result.size() == 0) {
            throw new IllegalStateException(MessageFormat
                .format("no author with email: \"{0}\"", email));
        } else if (result.size() > 1) {
            throw new IllegalStateException(MessageFormat
                .format("more than one author with email: \"{0}\"", email));
        }
        return result.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Author> getPaperAuthors(long paperId) {
        log.debug("getPaperAuthors: paperId={}", paperId);

        Criteria crit = getSession().createCriteria(Author.class);
        crit.createCriteria("papers").add(Restrictions.eq("id", paperId));
        return new HashSet<Author>(crit.list());
    }

}
