package sk.styk.martin.apkanalyzer.dao.generic;

import java.util.List;

public interface GenericQueryableDao<T, U> {

    T find(U id);

    List<T> findAll();

}