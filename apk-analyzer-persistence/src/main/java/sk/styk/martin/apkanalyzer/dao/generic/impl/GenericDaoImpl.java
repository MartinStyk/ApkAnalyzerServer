package sk.styk.martin.apkanalyzer.dao.generic.impl;

import sk.styk.martin.apkanalyzer.dao.generic.GenericDao;

import java.io.Serializable;

/**
 * @author <a href="mailto:martin.styk@gmail.com">Martin Styk</a>
 */
public abstract class GenericDaoImpl<T, U> extends GenericQueryableDaoImpl<T, U> implements GenericDao<T, U>, Serializable {

    public GenericDaoImpl(Class<T> entityType) {
        super(entityType);
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

}