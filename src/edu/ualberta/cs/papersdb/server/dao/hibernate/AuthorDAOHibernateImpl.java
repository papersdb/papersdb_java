package edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.text.MessageFormat;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.server.dao.AuthorDAO;

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
        log.trace("getByFamilyName: {}", familyName);

        Author author = new Author();
        author.setFamilyNames(familyName);
        Set<Author> result = findByExample(author);
        if (result.size() == 0) {
            throw new IllegalStateException(MessageFormat
                .format("no author with family name: \"{0}\"",
                    familyName));
        } else if (result.size() > 1) {
            throw new IllegalStateException(MessageFormat
                .format("more than one author with family name: \"{0}\"",
                    familyName));
        }
        return result.iterator().next();
    }

    @Override
    public Set<Author> getAuthorsMatchingFamilyName(String familyName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Author getByEmail(String email) {
        // TODO Auto-generated method stub
        return null;
    }

}
