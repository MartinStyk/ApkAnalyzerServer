package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.transfer.object.UsageStatistics;

/**
 * @author mstyk
 * @date 11/13/17
 */
public interface UsageStatisticsService {

    UsageStatistics getAll();

    long numberOfUploadedApps();

    long numberOfDistinctUploadedApps();

    long numberOfDistinctDevices();

    long uploadsByDevice(String androidId);

    long totalFileHashes();

}
