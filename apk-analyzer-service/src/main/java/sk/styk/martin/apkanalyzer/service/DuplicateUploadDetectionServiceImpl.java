package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.dao.AppDataDao;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author mstyk
 * @date 11/13/17
 */
@Stateless
public class DuplicateUploadDetectionServiceImpl implements DuplicateUploadDetectionService {

    @Inject
    private AppDataDao appDataDao;

    public boolean isAlreadyUploaded(AppData data) {
        return appDataDao.find(data.getAndroidId(), data.getHash()) != null;
    }
}
