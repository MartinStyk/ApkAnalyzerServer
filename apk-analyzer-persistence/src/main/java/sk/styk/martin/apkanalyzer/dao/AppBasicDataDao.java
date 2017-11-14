package sk.styk.martin.apkanalyzer.dao;

import sk.styk.martin.apkanalyzer.dao.generic.GenericQueryableDao;
import sk.styk.martin.apkanalyzer.entity.AppBasicData;
import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

public interface AppBasicDataDao extends GenericQueryableDao<AppBasicData, Long> {

    List<AppBasicData> getForDevice(String androidId);

}