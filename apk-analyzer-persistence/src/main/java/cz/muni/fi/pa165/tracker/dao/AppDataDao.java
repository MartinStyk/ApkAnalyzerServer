package cz.muni.fi.pa165.tracker.dao;

import cz.muni.fi.pa165.tracker.entity.AppData;

import java.util.List;

/**
 * Interface represents data access object of app data
 *
 * @author Martin Styk
 * @version 20.11.2017
 */
public interface AppDataDao {

    void create(AppData appData);

    AppData update(AppData appData);

    void remove(long id);

    AppData find(long id);

    List<AppData> findAll();

    List<AppData> getForDevice(String deviceId);

}
