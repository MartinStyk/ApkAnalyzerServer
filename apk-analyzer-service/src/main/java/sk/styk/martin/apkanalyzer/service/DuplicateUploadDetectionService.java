package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.entity.AppData;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface DuplicateUploadDetectionService {

    boolean isAlreadyUploaded(AppData data);
}
