package edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Publisher;
import edu.ualberta.cs.papersdb.model.publication.Publication;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

@Repository
public class PaperDAOHibernateImpl
    extends GenericHibernateDAO<Paper, Long>
    implements PaperDAO {

    // private static final Logger LOG = Logger
    // .getLogger(PaperDAOHibernateImpl.class);

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.session = sessionFactory.getCurrentSession();
    }

    @Override
    public Paper getPaperForTitle(String title) {
        DetachedCriteria crit = DetachedCriteria.forClass(Paper.class);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Paper save(Paper paper) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Paper> getPapersMatching(String match, int start, int max) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Paper> getPapersForAuthor(long authorId, int start, int max) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Author> getAuthors(long paperId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Publisher getPublisher(long paperId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Publication getPublication(long paperId) {
        // TODO Auto-generated method stub
        return null;
    }

}
