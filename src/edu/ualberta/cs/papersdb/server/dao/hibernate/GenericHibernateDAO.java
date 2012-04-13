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
    public T findById(ID id, boolean lock) {
        if (log.isTraceEnabled())
            log.trace("findById: {} id={}", persistentClass.getClass(),
                id);
        return (T) getSession().load(getPersistentClass(), id);
    }

    public Set<T> findAll() {
        if (log.isTraceEnabled())
            log.trace("findAll: {}", persistentClass.getClass());
        return new HashSet<T>(findByCriteria());
    }

    @SuppressWarnings("unchecked")
    public Set<T> findByExample(T exampleInstance, String[] excludeProperty) {
        if (log.isTraceEnabled())
            log.trace("findByExample: {}", persistentClass);
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);

        if (excludeProperty != null) {
            for (String exclude : excludeProperty) {
                example.excludeProperty(exclude);
            }
        }
        crit.add(example);
        return new HashSet<T>(crit.list());
    }

    public T save(T entity) {
        if (log.isTraceEnabled())
            log.trace("save: {}", persistentClass);
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void delete(T entity) {
        if (log.isTraceEnabled())
            log.trace("delete: {}", persistentClass);
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        if (log.isTraceEnabled())
            log.trace("findByCriteria: {}", persistentClass);
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

}