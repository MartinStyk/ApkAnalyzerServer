package sk.styk.martin.apkanalyzer.dao;

import java.util.List;

public interface GenericDao<T, U> {

    T create(final T entity);

    T update(final T entity);

    void remove(U id);

    T find(U id);

    List<T> findAll();

}