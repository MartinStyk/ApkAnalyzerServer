package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.entity.AppData;
import sk.styk.martin.apkanalyzer.service.generic.GenericCRUDService;

import java.util.List;

public interface AppDataService extends GenericCRUDService<AppData, Long> {

    AppData createWithExistenceCheck(AppData appData);

    List<AppData> findByDevice(String deviceId);
}
