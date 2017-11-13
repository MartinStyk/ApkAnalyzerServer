package sk.styk.martin.apkanalyzer.transfer.object;

/**
 * @author mstyk
 * @date 11/13/17
 */
public class UsageStatistics {

    private long numberOfUploadedApps;

    private long numberOfDistinctUploadedApps;

    private long numberOfDistinctDevices;

    private long totalFileHashes;

    public UsageStatistics(long numberOfUploadedApps, long numberOfDistinctUploadedApps, long numberOfDistinctDevices, long totalFileHashes) {
        this.numberOfUploadedApps = numberOfUploadedApps;
        this.numberOfDistinctUploadedApps = numberOfDistinctUploadedApps;
        this.numberOfDistinctDevices = numberOfDistinctDevices;
        this.totalFileHashes = totalFileHashes;
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

    public long getTotalFileHashes() {
        return totalFileHashes;
    }
}
