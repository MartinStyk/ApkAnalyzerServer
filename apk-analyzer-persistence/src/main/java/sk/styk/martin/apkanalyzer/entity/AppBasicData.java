package sk.styk.martin.apkanalyzer.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Martin Styk
 * @version 11.11.2017
 */
public class AppBasicData {

    private Long id;
    private String androidId;
    private int hash;

    @Enumerated(EnumType.STRING)
    private AppData.AnalysisMode analysisMode;

    // GeneralData
    private String packageName;
    private String applicationName;
    private String versionName;
    private int versionCode;

    @Enumerated(EnumType.STRING)
    private AppData.AppSource source;
    private long apkSize;
    private int minSdkVersion;
    private int targetSdkVersion;

    // CertificateData
    private String certMd5;
    // Activities
    private int numberActivities;
    private int activitiesAggregatedHash;

    // Services
    private int numberServices;
    private int servicesAggregatedHash;

    // Content Providers
    private int numberContentProviders;
    private int contentProvidersAggregatedHash;

    // Broadcast Receivers
    private int numberBroadcastReceivers;
    private int broadcastReceiversAggregatedHash;

    // Defined permissions
    private int numberDefinedPermissions;
    private int definedPermissionsAggregatedHash;

    // Used permissions
    private int numberUsedPermissions;
    private int usedPermissionsAggregatedHash;

    // Features
    private int numberFeatures;
    private int featuresAggregatedHash;

    // FileData
    private String dexHash;
    private String arscHash;

    private int numberDrawables;
    private int numberLayouts;
    private int numberAssets;
    private int numberOthers;
    private int drawablesAggregatedHash;
    private int layoutsAggregatedHash;
    private int assetsAggregatedHash;
    private int otherAggregatedHash;

    //ResourceData
    private int numberDifferentDrawables;

    private int numberDifferentLayouts;

    // ClassPathData
    private int packageClassesAggregatedHash;
    private int numberPackageClasses;

    private int otherClassesAggregatedHash;
    private int numberOtherClasses;

    public AppBasicData(Long id, String androidId, int hash, AppData.AnalysisMode analysisMode, String packageName, String applicationName, String versionName, int versionCode, AppData.AppSource source, long apkSize, int minSdkVersion, int targetSdkVersion, String certMd5, int numberActivities, int activitiesAggregatedHash, int numberServices, int servicesAggregatedHash, int numberContentProviders, int contentProvidersAggregatedHash, int numberBroadcastReceivers, int broadcastReceiversAggregatedHash, int numberDefinedPermissions, int definedPermissionsAggregatedHash, int numberUsedPermissions, int usedPermissionsAggregatedHash, int numberFeatures, int featuresAggregatedHash, String dexHash, String arscHash, int numberDrawables, int numberLayouts, int numberAssets, int numberOthers, int drawablesAggregatedHash, int layoutsAggregatedHash, int assetsAggregatedHash, int otherAggregatedHash, int numberDifferentDrawables, int numberDifferentLayouts, int packageClassesAggregatedHash, int numberPackageClasses, int otherClassesAggregatedHash, int numberOtherClasses) {
        this.id = id;
        this.androidId = androidId;
        this.hash = hash;
        this.analysisMode = analysisMode;
        this.packageName = packageName;
        this.applicationName = applicationName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.source = source;
        this.apkSize = apkSize;
        this.minSdkVersion = minSdkVersion;
        this.targetSdkVersion = targetSdkVersion;
        this.certMd5 = certMd5;
        this.numberActivities = numberActivities;
        this.activitiesAggregatedHash = activitiesAggregatedHash;
        this.numberServices = numberServices;
        this.servicesAggregatedHash = servicesAggregatedHash;
        this.numberContentProviders = numberContentProviders;
        this.contentProvidersAggregatedHash = contentProvidersAggregatedHash;
        this.numberBroadcastReceivers = numberBroadcastReceivers;
        this.broadcastReceiversAggregatedHash = broadcastReceiversAggregatedHash;
        this.numberDefinedPermissions = numberDefinedPermissions;
        this.definedPermissionsAggregatedHash = definedPermissionsAggregatedHash;
        this.numberUsedPermissions = numberUsedPermissions;
        this.usedPermissionsAggregatedHash = usedPermissionsAggregatedHash;
        this.numberFeatures = numberFeatures;
        this.featuresAggregatedHash = featuresAggregatedHash;
        this.dexHash = dexHash;
        this.arscHash = arscHash;
        this.numberDrawables = numberDrawables;
        this.numberLayouts = numberLayouts;
        this.numberAssets = numberAssets;
        this.numberOthers = numberOthers;
        this.drawablesAggregatedHash = drawablesAggregatedHash;
        this.layoutsAggregatedHash = layoutsAggregatedHash;
        this.assetsAggregatedHash = assetsAggregatedHash;
        this.otherAggregatedHash = otherAggregatedHash;
        this.numberDifferentDrawables = numberDifferentDrawables;
        this.numberDifferentLayouts = numberDifferentLayouts;
        this.packageClassesAggregatedHash = packageClassesAggregatedHash;
        this.numberPackageClasses = numberPackageClasses;
        this.otherClassesAggregatedHash = otherClassesAggregatedHash;
        this.numberOtherClasses = numberOtherClasses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public AppData.AnalysisMode getAnalysisMode() {
        return analysisMode;
    }

    public void setAnalysisMode(AppData.AnalysisMode analysisMode) {
        this.analysisMode = analysisMode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public AppData.AppSource getSource() {
        return source;
    }

    public void setSource(AppData.AppSource source) {
        this.source = source;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getCertMd5() {
        return certMd5;
    }

    public void setCertMd5(String certMd5) {
        this.certMd5 = certMd5;
    }

    public int getNumberActivities() {
        return numberActivities;
    }

    public void setNumberActivities(int numberActivities) {
        this.numberActivities = numberActivities;
    }

    public int getActivitiesAggregatedHash() {
        return activitiesAggregatedHash;
    }

    public void setActivitiesAggregatedHash(int activitiesAggregatedHash) {
        this.activitiesAggregatedHash = activitiesAggregatedHash;
    }

    public int getNumberServices() {
        return numberServices;
    }

    public void setNumberServices(int numberServices) {
        this.numberServices = numberServices;
    }

    public int getServicesAggregatedHash() {
        return servicesAggregatedHash;
    }

    public void setServicesAggregatedHash(int servicesAggregatedHash) {
        this.servicesAggregatedHash = servicesAggregatedHash;
    }

    public int getNumberContentProviders() {
        return numberContentProviders;
    }

    public void setNumberContentProviders(int numberContentProviders) {
        this.numberContentProviders = numberContentProviders;
    }

    public int getContentProvidersAggregatedHash() {
        return contentProvidersAggregatedHash;
    }

    public void setContentProvidersAggregatedHash(int contentProvidersAggregatedHash) {
        this.contentProvidersAggregatedHash = contentProvidersAggregatedHash;
    }

    public int getNumberBroadcastReceivers() {
        return numberBroadcastReceivers;
    }

    public void setNumberBroadcastReceivers(int numberBroadcastReceivers) {
        this.numberBroadcastReceivers = numberBroadcastReceivers;
    }

    public int getBroadcastReceiversAggregatedHash() {
        return broadcastReceiversAggregatedHash;
    }

    public void setBroadcastReceiversAggregatedHash(int broadcastReceiversAggregatedHash) {
        this.broadcastReceiversAggregatedHash = broadcastReceiversAggregatedHash;
    }

    public int getNumberDefinedPermissions() {
        return numberDefinedPermissions;
    }

    public void setNumberDefinedPermissions(int numberDefinedPermissions) {
        this.numberDefinedPermissions = numberDefinedPermissions;
    }

    public int getDefinedPermissionsAggregatedHash() {
        return definedPermissionsAggregatedHash;
    }

    public void setDefinedPermissionsAggregatedHash(int definedPermissionsAggregatedHash) {
        this.definedPermissionsAggregatedHash = definedPermissionsAggregatedHash;
    }

    public int getNumberUsedPermissions() {
        return numberUsedPermissions;
    }

    public void setNumberUsedPermissions(int numberUsedPermissions) {
        this.numberUsedPermissions = numberUsedPermissions;
    }

    public int getUsedPermissionsAggregatedHash() {
        return usedPermissionsAggregatedHash;
    }

    public void setUsedPermissionsAggregatedHash(int usedPermissionsAggregatedHash) {
        this.usedPermissionsAggregatedHash = usedPermissionsAggregatedHash;
    }

    public int getNumberFeatures() {
        return numberFeatures;
    }

    public void setNumberFeatures(int numberFeatures) {
        this.numberFeatures = numberFeatures;
    }

    public int getFeaturesAggregatedHash() {
        return featuresAggregatedHash;
    }

    public void setFeaturesAggregatedHash(int featuresAggregatedHash) {
        this.featuresAggregatedHash = featuresAggregatedHash;
    }

    public String getDexHash() {
        return dexHash;
    }

    public void setDexHash(String dexHash) {
        this.dexHash = dexHash;
    }

    public String getArscHash() {
        return arscHash;
    }

    public void setArscHash(String arscHash) {
        this.arscHash = arscHash;
    }

    public int getNumberDrawables() {
        return numberDrawables;
    }

    public void setNumberDrawables(int numberDrawables) {
        this.numberDrawables = numberDrawables;
    }

    public int getNumberLayouts() {
        return numberLayouts;
    }

    public void setNumberLayouts(int numberLayouts) {
        this.numberLayouts = numberLayouts;
    }

    public int getNumberAssets() {
        return numberAssets;
    }

    public void setNumberAssets(int numberAssets) {
        this.numberAssets = numberAssets;
    }

    public int getNumberOthers() {
        return numberOthers;
    }

    public void setNumberOthers(int numberOthers) {
        this.numberOthers = numberOthers;
    }

    public int getDrawablesAggregatedHash() {
        return drawablesAggregatedHash;
    }

    public void setDrawablesAggregatedHash(int drawablesAggregatedHash) {
        this.drawablesAggregatedHash = drawablesAggregatedHash;
    }

    public int getLayoutsAggregatedHash() {
        return layoutsAggregatedHash;
    }

    public void setLayoutsAggregatedHash(int layoutsAggregatedHash) {
        this.layoutsAggregatedHash = layoutsAggregatedHash;
    }

    public int getAssetsAggregatedHash() {
        return assetsAggregatedHash;
    }

    public void setAssetsAggregatedHash(int assetsAggregatedHash) {
        this.assetsAggregatedHash = assetsAggregatedHash;
    }

    public int getOtherAggregatedHash() {
        return otherAggregatedHash;
    }

    public void setOtherAggregatedHash(int otherAggregatedHash) {
        this.otherAggregatedHash = otherAggregatedHash;
    }

    public int getNumberDifferentDrawables() {
        return numberDifferentDrawables;
    }

    public void setNumberDifferentDrawables(int numberDifferentDrawables) {
        this.numberDifferentDrawables = numberDifferentDrawables;
    }

    public int getNumberDifferentLayouts() {
        return numberDifferentLayouts;
    }

    public void setNumberDifferentLayouts(int numberDifferentLayouts) {
        this.numberDifferentLayouts = numberDifferentLayouts;
    }

    public int getPackageClassesAggregatedHash() {
        return packageClassesAggregatedHash;
    }

    public void setPackageClassesAggregatedHash(int packageClassesAggregatedHash) {
        this.packageClassesAggregatedHash = packageClassesAggregatedHash;
    }

    public int getNumberPackageClasses() {
        return numberPackageClasses;
    }

    public void setNumberPackageClasses(int numberPackageClasses) {
        this.numberPackageClasses = numberPackageClasses;
    }

    public int getOtherClassesAggregatedHash() {
        return otherClassesAggregatedHash;
    }

    public void setOtherClassesAggregatedHash(int otherClassesAggregatedHash) {
        this.otherClassesAggregatedHash = otherClassesAggregatedHash;
    }

    public int getNumberOtherClasses() {
        return numberOtherClasses;
    }

    public void setNumberOtherClasses(int numberOtherClasses) {
        this.numberOtherClasses = numberOtherClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppBasicData that = (AppBasicData) o;

        if (hash != that.hash) return false;
        if (versionCode != that.versionCode) return false;
        if (apkSize != that.apkSize) return false;
        if (minSdkVersion != that.minSdkVersion) return false;
        if (targetSdkVersion != that.targetSdkVersion) return false;
        if (numberActivities != that.numberActivities) return false;
        if (activitiesAggregatedHash != that.activitiesAggregatedHash) return false;
        if (numberServices != that.numberServices) return false;
        if (servicesAggregatedHash != that.servicesAggregatedHash) return false;
        if (numberContentProviders != that.numberContentProviders) return false;
        if (contentProvidersAggregatedHash != that.contentProvidersAggregatedHash) return false;
        if (numberBroadcastReceivers != that.numberBroadcastReceivers) return false;
        if (broadcastReceiversAggregatedHash != that.broadcastReceiversAggregatedHash) return false;
        if (numberDefinedPermissions != that.numberDefinedPermissions) return false;
        if (definedPermissionsAggregatedHash != that.definedPermissionsAggregatedHash) return false;
        if (numberUsedPermissions != that.numberUsedPermissions) return false;
        if (usedPermissionsAggregatedHash != that.usedPermissionsAggregatedHash) return false;
        if (numberFeatures != that.numberFeatures) return false;
        if (featuresAggregatedHash != that.featuresAggregatedHash) return false;
        if (numberDrawables != that.numberDrawables) return false;
        if (numberLayouts != that.numberLayouts) return false;
        if (numberAssets != that.numberAssets) return false;
        if (numberOthers != that.numberOthers) return false;
        if (drawablesAggregatedHash != that.drawablesAggregatedHash) return false;
        if (layoutsAggregatedHash != that.layoutsAggregatedHash) return false;
        if (assetsAggregatedHash != that.assetsAggregatedHash) return false;
        if (otherAggregatedHash != that.otherAggregatedHash) return false;
        if (numberDifferentDrawables != that.numberDifferentDrawables) return false;
        if (numberDifferentLayouts != that.numberDifferentLayouts) return false;
        if (packageClassesAggregatedHash != that.packageClassesAggregatedHash) return false;
        if (numberPackageClasses != that.numberPackageClasses) return false;
        if (otherClassesAggregatedHash != that.otherClassesAggregatedHash) return false;
        if (numberOtherClasses != that.numberOtherClasses) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (androidId != null ? !androidId.equals(that.androidId) : that.androidId != null) return false;
        if (analysisMode != that.analysisMode) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (versionName != null ? !versionName.equals(that.versionName) : that.versionName != null) return false;
        if (source != that.source) return false;
        if (certMd5 != null ? !certMd5.equals(that.certMd5) : that.certMd5 != null) return false;
        if (dexHash != null ? !dexHash.equals(that.dexHash) : that.dexHash != null) return false;
        return arscHash != null ? arscHash.equals(that.arscHash) : that.arscHash == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (androidId != null ? androidId.hashCode() : 0);
        result = 31 * result + hash;
        result = 31 * result + (analysisMode != null ? analysisMode.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + versionCode;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (int) (apkSize ^ (apkSize >>> 32));
        result = 31 * result + minSdkVersion;
        result = 31 * result + targetSdkVersion;
        result = 31 * result + (certMd5 != null ? certMd5.hashCode() : 0);
        result = 31 * result + numberActivities;
        result = 31 * result + activitiesAggregatedHash;
        result = 31 * result + numberServices;
        result = 31 * result + servicesAggregatedHash;
        result = 31 * result + numberContentProviders;
        result = 31 * result + contentProvidersAggregatedHash;
        result = 31 * result + numberBroadcastReceivers;
        result = 31 * result + broadcastReceiversAggregatedHash;
        result = 31 * result + numberDefinedPermissions;
        result = 31 * result + definedPermissionsAggregatedHash;
        result = 31 * result + numberUsedPermissions;
        result = 31 * result + usedPermissionsAggregatedHash;
        result = 31 * result + numberFeatures;
        result = 31 * result + featuresAggregatedHash;
        result = 31 * result + (dexHash != null ? dexHash.hashCode() : 0);
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + numberDrawables;
        result = 31 * result + numberLayouts;
        result = 31 * result + numberAssets;
        result = 31 * result + numberOthers;
        result = 31 * result + drawablesAggregatedHash;
        result = 31 * result + layoutsAggregatedHash;
        result = 31 * result + assetsAggregatedHash;
        result = 31 * result + otherAggregatedHash;
        result = 31 * result + numberDifferentDrawables;
        result = 31 * result + numberDifferentLayouts;
        result = 31 * result + packageClassesAggregatedHash;
        result = 31 * result + numberPackageClasses;
        result = 31 * result + otherClassesAggregatedHash;
        result = 31 * result + numberOtherClasses;
        return result;
    }
}