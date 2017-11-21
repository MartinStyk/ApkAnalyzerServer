package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.dto.UsageStatisticsDto;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface UsageStatisticsService {

    UsageStatisticsDto getAll();

    long numberOfUploadedApps();

    long numberOfDistinctUploadedApps();

    long numberOfDistinctDevices();

    long uploadsByDevice(String androidId);

}