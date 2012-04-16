package edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ualberta.cs.papersdb.server.dao.GenericDAO;

public class GenericHibernateDAO<T, ID extends Serializable>
    implements GenericDAO<T, ID> {

    public static Logger log = LoggerFactory
        .getLogger(GenericHibernateDAO.class.getName());

    private Class<T> persistentClass;
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setSession(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    protected Session getSession() {
        if (sessionFactory == null)
            throw new IllegalStateException(
                "Session has not been set on DAO before usage");
        return sessionFactory.getCurrentSession();
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id) {
        if (log.isDebugEnabled())
            log.debug("findById: {} id={}", persistentClass.getClass(),
                id);
        return (T) getSession().load(getPersistentClass(), id);
    }

    public Set<T> findAll() {
        if (log.isDebugEnabled())
            log.debug("findAll: {}", persistentClass.getClass());
        return new HashSet<T>(findByCriteria());
    }

    @SuppressWarnings("unchecked")
    public Set<T> findByExample(T exampleInstance, String... excludeProperties) {
        if (log.isDebugEnabled())
            log.debug("findByExample: {}", persistentClass);
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);

        if (excludeProperties != null) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        criteria.add(example);
        return new HashSet<T>(criteria.list());
    }

    public T save(T entity) {
        if (log.isDebugEnabled())
            log.debug("save: {}", persistentClass);
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void delete(T entity) {
        if (log.isDebugEnabled())
            log.debug("delete: {}", persistentClass);
        getSession().delete(entity);
    }

    @Override
    public void flush() {
        log.debug("flush");
        getSession().flush();
    }

    @Override
    public void clear() {
        log.debug("clear");
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        if (log.isDebugEnabled())
            log.debug("findByCriteria: {}", persistentClass);
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

}