package sk.styk.martin.apkanalyzer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.styk.martin.apkanalyzer.dao.UsageStatisticsDao;
import sk.styk.martin.apkanalyzer.dto.UsageStatisticsDto;

import javax.inject.Inject;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
@Transactional
public class UsageStatisticsServiceImpl implements UsageStatisticsService {
    @Inject
    private UsageStatisticsDao usageStatisticsDao;

    @Override
    public UsageStatisticsDto getAll() {
        return new UsageStatisticsDto(
                numberOfUploadedApps(),
                numberOfDistinctUploadedApps(),
                numberOfDistinctDevices()
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
}
