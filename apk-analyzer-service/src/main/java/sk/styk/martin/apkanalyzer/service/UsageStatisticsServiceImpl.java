package sk.styk.martin.apkanalyzer.service;

import sk.styk.martin.apkanalyzer.dao.UsageStatisticsDao;
import sk.styk.martin.apkanalyzer.transfer.object.UsageStatistics;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author mstyk
 * @date 11/13/17
 */
@Stateless
public class UsageStatisticsServiceImpl implements UsageStatisticsService {

    @Inject
    private UsageStatisticsDao usageStatisticsDao;

    @Override
    public UsageStatistics getAll() {
        return new UsageStatistics(
              numberOfUploadedApps(),
              numberOfDistinctUploadedApps(),
              numberOfDistinctDevices(),
              totalFileHashes()
        );
    }

    @Override
    public long numberOfUploadedApps() {
        return usageStatisticsDao.numberOfUploadedApps();
    }

    @Override
    public long numberOfDistinctUploadedApps() {
        return usageStatisticsDao.numberOfDistinctUploadedApps();
    }

    @Override
    public long numberOfDistinctDevices() {
        return usageStatisticsDao.numberOfDistinctDevices();
    }

    @Override
    public long uploadsByDevice(String androidId) {
        return usageStatisticsDao.uploadsByDevice(androidId);
    }

    @Override
    public long totalFileHashes() {
        return usageStatisticsDao.totalFileHashes();
    }
}
