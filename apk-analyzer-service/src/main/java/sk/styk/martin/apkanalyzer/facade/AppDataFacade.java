package sk.styk.martin.apkanalyzer.facade;

import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface AppDataFacade {
    AppData create(AppData entity);

    AppData update(AppData entity);

    void remove(AppData entity);

    AppData findById(Long id);

    List<AppData> findAll();

    AppData createWithExistenceCheck(AppData appData);

    List<AppData> findByDevice(String deviceId);
}
