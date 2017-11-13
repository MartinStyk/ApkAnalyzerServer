package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.dao.AppDataDao;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AppDataServiceImpl implements AppDataService {

    @Inject
    private AppDataDao appDataDao;

    @Inject
    private DuplicateUploadDetectionService duplicateDetectionService;

    @Inject
    private TimeService timeService;

    @Override
    public AppData create(AppData appData) {
        appData.setUploadTime(timeService.getCurrentDate());
        AppData created = appDataDao.create(appData);
        return created;
    }

    @Override
    public AppData createWithExistenceCheck(AppData appData) {
        return duplicateDetectionService.isAlreadyUploaded(appData) ? null : create(appData);
    }

    @Override
    public AppData update(AppData appData) {
        AppData updated = appDataDao.update(appData);
        return updated;
    }

    @Override
    public void remove(AppData appData) throws IllegalArgumentException {
        appDataDao.remove(appData.getId());
    }

    @Override
    public AppData findById(Long id) {
        return appDataDao.find(id);
    }

    @Override
    public List<AppData> findAll() {
        return appDataDao.findAll();
    }

    @Override
    public List<AppData> findByDevice(String deviceId) {
        return appDataDao.getForDevice(deviceId);
    }
}
