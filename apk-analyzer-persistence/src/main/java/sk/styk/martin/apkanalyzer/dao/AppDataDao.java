package sk.styk.martin.apkanalyzer.dao;

import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

/**
 * Interface represents data access object of app data
 *
 * @author Martin Styk
 * @version 20.11.2017
 */
public interface AppDataDao {

    AppData create(AppData appData);

    AppData update(AppData appData);

    void remove(long id);

    AppData find(long id);

    List<AppData> findAll();

    AppData find(String deviceId, int dataHash);

    List<AppData> getForDevice(String deviceId);

}
