package sk.styk.martin.apkanalyzer.service;

import org.springframework.stereotype.Service;
import sk.styk.martin.apkanalyzer.dao.AppDataDao;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.inject.Inject;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
public class DuplicateUploadDetectionServiceImpl implements DuplicateUploadDetectionService {

    @Inject
    private AppDataDao appDataDao;

    public boolean isAlreadyUploaded(AppData data) {
        return appDataDao.find(data.getAndroidId(), data.getHash()) != null;
    }
}
