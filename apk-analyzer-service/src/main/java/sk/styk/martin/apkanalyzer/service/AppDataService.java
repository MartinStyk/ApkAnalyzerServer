package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

public interface AppDataService {

    AppData create(AppData entity);

    AppData update(AppData entity);

    void remove(AppData entity);

    AppData findById(Long id);

    List<AppData> findAll();

    AppData createWithExistenceCheck(AppData appData);

    List<AppData> findByDevice(String deviceId);
}