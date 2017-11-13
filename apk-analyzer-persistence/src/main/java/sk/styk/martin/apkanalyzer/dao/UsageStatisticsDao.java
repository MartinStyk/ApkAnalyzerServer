package sk.styk.martin.apkanalyzer.dao;

import org.hibernate.validator.constraints.NotEmpty;
import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

public interface UsageStatisticsDao {

    long numberOfUploadedApps();

    long numberOfDistinctUploadedApps();

    long numberOfDistinctDevices();

    long uploadsByDevice(String androidId);

    long totalFileHashes();
}