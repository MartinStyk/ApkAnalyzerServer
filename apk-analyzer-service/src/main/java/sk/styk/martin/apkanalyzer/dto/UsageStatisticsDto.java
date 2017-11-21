package sk.styk.martin.apkanalyzer.dto;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public class UsageStatisticsDto {
    private long numberOfUploadedApps;

    private long numberOfDistinctUploadedApps;

    private long numberOfDistinctDevices;

    public UsageStatisticsDto(long numberOfUploadedApps, long numberOfDistinctUploadedApps, long numberOfDistinctDevices) {
        this.numberOfUploadedApps = numberOfUploadedApps;
        this.numberOfDistinctUploadedApps = numberOfDistinctUploadedApps;
        this.numberOfDistinctDevices = numberOfDistinctDevices;
    }

    public long getNumberOfUploadedApps() {
        return numberOfUploadedApps;
    }

    public long getNumberOfDistinctUploadedApps() {
        return numberOfDistinctUploadedApps;
    }

    public long getNumberOfDistinctDevices() {
        return numberOfDistinctDevices;
    }
}
