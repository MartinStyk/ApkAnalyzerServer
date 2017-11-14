package sk.styk.martin.apkanalyzer.dao.generic.impl;

import sk.styk.martin.apkanalyzer.dao.generic.GenericQueryableDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:martin.styk@gmail.com">Martin Styk</a>
 */
public abstract class GenericQueryableDaoImpl<T, U> implements GenericQueryableDao<T, U>, Serializable {

    @Inject
    protected EntityManager em;

    protected Class<T> type;

    public GenericQueryableDaoImpl(Class<T> entityType) {
        type = entityType;
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