package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.entity.AppBasicData;
import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

public interface AppBasicDataService {

    AppBasicData findById(Long id);

    List<AppBasicData> findAll();

    List<AppBasicData> findByDevice(String deviceId);
}
