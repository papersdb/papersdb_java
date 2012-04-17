package edu.ualberta.cs.papersdb.server.dao.hibernate;

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

import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

//@Transactional
@Repository
public class PaperDAOHibernateImpl
    extends GenericHibernateDAO<Paper, Long>
    implements PaperDAO {

    private static final Logger log = LoggerFactory.getLogger(
        PaperDAOHibernateImpl.class);

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Paper getByTitle(String title) {
        log.debug("getByTitle: {}", title);

        Paper paper = new Paper();
        paper.setTitle(title);
        Set<Paper> result = findByExample(paper, "isPublic");
        if (result.size() > 1) {
            // should never happen because title is a unique key
            throw new IllegalStateException(MessageFormat
                .format("more than one paper with title: \"{0}\"",
                    title));
        } else if (result.size() == 0) {
            return null;
        }
        return result.iterator().next();
    }

    @Override
    public Paper getByDoi(String doi) {
        log.debug("getByDoi: {}", doi);

        Paper paper = new Paper();
        paper.setDOI(doi);
        Set<Paper> result = findByExample(paper, "isPublic");
        if (result.size() == 0) {
            throw new IllegalStateException(MessageFormat
                .format("no paper with DOI: \"{0}\"",
                    doi));
        } else if (result.size() > 1) {
            throw new IllegalStateException(MessageFormat
                .format("more than one paper with DOI: \"{0}\"",
                    doi));
        }
        return result.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Paper> getPapersTitleMatching(String match, int start, int max) {
        log.debug("getPapersTitleMatching: match={} start={} max={}",
            new Object[] { match, start, max });

        Criteria crit = getSession().createCriteria(Paper.class);
        crit.add(Restrictions.ilike("title", match, MatchMode.ANYWHERE));
        crit.addOrder(Order.asc("title"));
        crit.setFirstResult(start);
        crit.setMaxResults(max);
        return new HashSet<Paper>(crit.list());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Paper> getForAuthor(long authorId, int start, int max) {
        Criteria crit = getSession().createCriteria(Paper.class);
        crit.createAlias("authors", "a");
        crit.add(Restrictions.eq("a.id", authorId));
        return new HashSet<Paper>(crit.list());
    }

}
