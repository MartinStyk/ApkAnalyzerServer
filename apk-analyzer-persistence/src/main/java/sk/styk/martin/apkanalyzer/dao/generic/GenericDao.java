package sk.styk.martin.apkanalyzer.dao.generic;

public interface GenericDao<T, U> extends GenericQueryableDao<T,U>{

    T create(final T entity);

    T update(final T entity);

    void remove(U id);

}