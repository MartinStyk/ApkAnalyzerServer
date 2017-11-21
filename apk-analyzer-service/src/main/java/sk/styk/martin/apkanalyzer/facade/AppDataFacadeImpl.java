package sk.styk.martin.apkanalyzer.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.styk.martin.apkanalyzer.entity.AppData;
import sk.styk.martin.apkanalyzer.service.AppDataService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
@Transactional
public class AppDataFacadeImpl implements AppDataFacade {

    @Inject
    private AppDataService appDataService;

    @Override
    public AppData create(AppData appData) {
        return appDataService.create(appData);
    }

    @Override
    public AppData createWithExistenceCheck(AppData appData) {
        return appDataService.createWithExistenceCheck(appData);
    }

    @Override
    public AppData update(AppData appData) {
        return appDataService.update(appData);

    }

    @Override
    public void remove(AppData appData) throws IllegalArgumentException {
        appDataService.remove(appData);
    }

    @Override
    public AppData findById(Long id) {
        return appDataService.findById(id);

    }

    @Override
    public List<AppData> findAll() {
        return appDataService.findAll();
    }

    @Override
    public List<AppData> findByDevice(String deviceId) {
        return appDataService.findByDevice(deviceId);
    }

}
