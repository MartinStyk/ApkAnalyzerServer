package sk.styk.martin.apkanalyzer.dao;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface UsageStatisticsDao {
    long numberOfUploadedApps();

    long numberOfDistinctUploadedApps();

    long numberOfDistinctDevices();

    long uploadsByDevice(String androidId);
}