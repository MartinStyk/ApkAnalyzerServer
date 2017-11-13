package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.entity.AppData;

/**
 * @author mstyk
 * @date 11/13/17
 */
public interface DuplicateUploadDetectionService {

    boolean isAlreadyUploaded(AppData data);
}
