package sk.styk.martin.apkanalyzer.dao.impl;

import sk.styk.martin.apkanalyzer.dao.GenericDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:martin.styk@gmail.com">Martin Styk</a>
 */
public abstract class GenericDaoImpl<T, U> implements GenericDao<T, U>, Serializable {

    @Inject
    protected EntityManager em;

    private Class<T> type;

    public GenericDaoImpl(Class<T> entityType) {
        type = entityType;
    }

    @Override
    public T create(final T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T update(final T entity) {
        return em.merge(entity);
    }

    @Override
    public void remove(U id) {
        em.remove(this.em.getReference(type, id));
    }

    @Override
    public T find(U id) {
        return em.find(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return em.createQuery("FROM " + type.getName()).<T>getResultList();

    }
}