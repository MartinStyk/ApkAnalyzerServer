package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.dao.AppBasicDataDao;
import sk.styk.martin.apkanalyzer.dao.AppDataDao;
import sk.styk.martin.apkanalyzer.entity.AppBasicData;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AppBasicDataServiceImpl implements AppBasicDataService {

    @Inject
    private AppBasicDataDao appBasicDataDao;

    @Override
    public AppBasicData findById(Long id) {
        return appBasicDataDao.find(id);
    }

    @Override
    public List<AppBasicData> findAll() {
        return appBasicDataDao.findAll();
    }

    @Override
    public List<AppBasicData> findByDevice(String deviceId) {
        return appBasicDataDao.getForDevice(deviceId);
    }
}
