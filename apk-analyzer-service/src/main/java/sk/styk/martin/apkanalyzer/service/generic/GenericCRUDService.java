package sk.styk.martin.apkanalyzer.service.generic;

import java.util.List;

/**
 * @param <T> entity type for the CRUD operations
 * @param <U> identification type of the entity
 */
public interface GenericCRUDService<T, U> {

    T create(T entity);

    T update(T entity);

    void remove(T entity);

    T findById(U id);

    List<T> findAll();
}
