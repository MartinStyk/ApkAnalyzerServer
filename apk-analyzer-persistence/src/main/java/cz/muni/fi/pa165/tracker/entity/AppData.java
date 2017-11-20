package cz.muni.fi.pa165.tracker.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Styk
 * @version 11.11.2017
 */
@Entity
public class AppData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;

    @NotEmpty
    @Column(nullable = false)
    private String androidId;

    // Hash of data structure, can be used to identify two exactly same apps
    private int hash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnalysisMode analysisMode;

    // GeneralData
    private String packageName;

    private String applicationName;

    private String versionName;

    private int versionCode;

    @Enumerated(EnumType.STRING)
    private AppSource source;

    private long apkSize;

    private int minSdkVersion;

    private int targetSdkVersion;

    // CertificateData
    private String signAlgorithm;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private String publicKeyMd5;

    private String certMd5;

    private int serialNumber;

    private String issuerName;

    private String issuerOrganization;

    private String issuerCountry;

    private String subjectName;

    private String subjectOrganization;

    private String subjectCountry;

    // Activities
    private int numberActivities;

    private int activitiesAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "Activities", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> activityNames;

    // Services
    private int numberServices;

    private int servicesAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "Services", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> serviceNames;

    // Content Providers
    private int numberContentProviders;

    private int contentProvidersAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "ContentProviders", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> contentProviderNames;

    // Broadcast Receivers
    private int numberBroadcastReceivers;

    private int broadcastReceiversAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "BroadcastReceivers", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> broadcastReceiverNames;

    // Defined permissions
    private int numberDefinedPermissions;

    private int definedPermissionsAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "DefinedPermissions", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> definedPermissions;

    // Used permissions
    private int numberUsedPermissions;

    private int usedPermissionsAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "UsedPermissions", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> usedPermissions;

    // Features
    private int numberFeatures;

    private int featuresAggregatedHash;

    @ElementCollection
    @CollectionTable(name = "Features", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> featureNames;

    // FileData
    private String dexHash;

    private String arscHash;

    @ElementCollection
    @CollectionTable(name = "DrawableFiles", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> drawableHashes;

    @ElementCollection
    @CollectionTable(name = "LayoutFiles", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> layoutHashes;

    @ElementCollection
    @CollectionTable(name = "AssetFiles", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> assetHashes;

    @ElementCollection
    @CollectionTable(name = "OtherFiles", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> otherHashes;

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

    private int pngDrawables;

    private int ninePatchDrawables;

    private int jpgDrawables;

    private int gifDrawables;

    private int xmlDrawables;

    private int ldpiDrawables;

    private int mdpiDrawables;

    private int hdpiDrawables;

    private int xhdpiDrawables;

    private int xxhdpiDrawables;

    private int xxxhdpiDrawables;

    private int nodpiDrawables;

    private int tvdpiDrawables;

    private int unspecifiedDpiDrawables;

    // ClassPathData
    @ElementCollection
    @CollectionTable(name = "PackageClasses", joinColumns = @JoinColumn(name = "appdata_id"))
    private List<String> packageClasses;

    private int packageClassesAggregatedHash;

    private int numberPackageClasses;

    private int otherClassesAggregatedHash;

    private int numberOtherClasses;

    public enum AnalysisMode {
        INSTALLED_PACKAGE,
        APK_FILE
    }

    public enum AppSource {

        GOOGLE_PLAY("Google Play"),
        AMAZON_STORE("Amazon App Store"),
        SYSTEM_PREINSTALED("Pre-installed"),
        UNKNOWN("Unknown");

        private String name;

        AppSource(String name) {
            this.name = name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppData appData = (AppData) o;

        if (hash != appData.hash) return false;
        if (versionCode != appData.versionCode) return false;
        if (apkSize != appData.apkSize) return false;
        if (minSdkVersion != appData.minSdkVersion) return false;
        if (targetSdkVersion != appData.targetSdkVersion) return false;
        if (numberActivities != appData.numberActivities) return false;
        if (activitiesAggregatedHash != appData.activitiesAggregatedHash) return false;
        if (numberServices != appData.numberServices) return false;
        if (servicesAggregatedHash != appData.servicesAggregatedHash) return false;
        if (numberContentProviders != appData.numberContentProviders) return false;
        if (contentProvidersAggregatedHash != appData.contentProvidersAggregatedHash) return false;
        if (numberBroadcastReceivers != appData.numberBroadcastReceivers) return false;
        if (broadcastReceiversAggregatedHash != appData.broadcastReceiversAggregatedHash) return false;
        if (numberDefinedPermissions != appData.numberDefinedPermissions) return false;
        if (definedPermissionsAggregatedHash != appData.definedPermissionsAggregatedHash) return false;
        if (numberUsedPermissions != appData.numberUsedPermissions) return false;
        if (usedPermissionsAggregatedHash != appData.usedPermissionsAggregatedHash) return false;
        if (numberFeatures != appData.numberFeatures) return false;
        if (featuresAggregatedHash != appData.featuresAggregatedHash) return false;
        if (numberDrawables != appData.numberDrawables) return false;
        if (numberLayouts != appData.numberLayouts) return false;
        if (numberAssets != appData.numberAssets) return false;
        if (numberOthers != appData.numberOthers) return false;
        if (drawablesAggregatedHash != appData.drawablesAggregatedHash) return false;
        if (layoutsAggregatedHash != appData.layoutsAggregatedHash) return false;
        if (assetsAggregatedHash != appData.assetsAggregatedHash) return false;
        if (otherAggregatedHash != appData.otherAggregatedHash) return false;
        if (numberDifferentDrawables != appData.numberDifferentDrawables) return false;
        if (numberDifferentLayouts != appData.numberDifferentLayouts) return false;
        if (pngDrawables != appData.pngDrawables) return false;
        if (ninePatchDrawables != appData.ninePatchDrawables) return false;
        if (jpgDrawables != appData.jpgDrawables) return false;
        if (gifDrawables != appData.gifDrawables) return false;
        if (xmlDrawables != appData.xmlDrawables) return false;
        if (ldpiDrawables != appData.ldpiDrawables) return false;
        if (mdpiDrawables != appData.mdpiDrawables) return false;
        if (hdpiDrawables != appData.hdpiDrawables) return false;
        if (xhdpiDrawables != appData.xhdpiDrawables) return false;
        if (xxhdpiDrawables != appData.xxhdpiDrawables) return false;
        if (xxxhdpiDrawables != appData.xxxhdpiDrawables) return false;
        if (nodpiDrawables != appData.nodpiDrawables) return false;
        if (tvdpiDrawables != appData.tvdpiDrawables) return false;
        if (unspecifiedDpiDrawables != appData.unspecifiedDpiDrawables) return false;
        if (packageClassesAggregatedHash != appData.packageClassesAggregatedHash) return false;
        if (numberPackageClasses != appData.numberPackageClasses) return false;
        if (otherClassesAggregatedHash != appData.otherClassesAggregatedHash) return false;
        if (numberOtherClasses != appData.numberOtherClasses) return false;
        if (id != null ? !id.equals(appData.id) : appData.id != null) return false;
        if (uploadTime != null ? !uploadTime.equals(appData.uploadTime) : appData.uploadTime != null) return false;
        if (androidId != null ? !androidId.equals(appData.androidId) : appData.androidId != null) return false;
        if (analysisMode != appData.analysisMode) return false;
        if (packageName != null ? !packageName.equals(appData.packageName) : appData.packageName != null) return false;
        if (applicationName != null ? !applicationName.equals(appData.applicationName) : appData.applicationName != null)
            return false;
        if (versionName != null ? !versionName.equals(appData.versionName) : appData.versionName != null) return false;
        if (source != appData.source) return false;
        if (signAlgorithm != null ? !signAlgorithm.equals(appData.signAlgorithm) : appData.signAlgorithm != null)
            return false;
        if (startDate != null ? !startDate.equals(appData.startDate) : appData.startDate != null) return false;
        if (endDate != null ? !endDate.equals(appData.endDate) : appData.endDate != null) return false;
        if (publicKeyMd5 != null ? !publicKeyMd5.equals(appData.publicKeyMd5) : appData.publicKeyMd5 != null)
            return false;
        if (certMd5 != null ? !certMd5.equals(appData.certMd5) : appData.certMd5 != null) return false;
        if (serialNumber != appData.serialNumber) return false;
        if (issuerName != null ? !issuerName.equals(appData.issuerName) : appData.issuerName != null) return false;
        if (issuerOrganization != null ? !issuerOrganization.equals(appData.issuerOrganization) : appData.issuerOrganization != null)
            return false;
        if (issuerCountry != null ? !issuerCountry.equals(appData.issuerCountry) : appData.issuerCountry != null)
            return false;
        if (subjectName != null ? !subjectName.equals(appData.subjectName) : appData.subjectName != null) return false;
        if (subjectOrganization != null ? !subjectOrganization.equals(appData.subjectOrganization) : appData.subjectOrganization != null)
            return false;
        if (subjectCountry != null ? !subjectCountry.equals(appData.subjectCountry) : appData.subjectCountry != null)
            return false;
        if (activityNames != null ? !activityNames.equals(appData.activityNames) : appData.activityNames != null)
            return false;
        if (serviceNames != null ? !serviceNames.equals(appData.serviceNames) : appData.serviceNames != null)
            return false;
        if (contentProviderNames != null ? !contentProviderNames.equals(appData.contentProviderNames) : appData.contentProviderNames != null)
            return false;
        if (broadcastReceiverNames != null ? !broadcastReceiverNames.equals(appData.broadcastReceiverNames) : appData.broadcastReceiverNames != null)
            return false;
        if (definedPermissions != null ? !definedPermissions.equals(appData.definedPermissions) : appData.definedPermissions != null)
            return false;
        if (usedPermissions != null ? !usedPermissions.equals(appData.usedPermissions) : appData.usedPermissions != null)
            return false;
        if (featureNames != null ? !featureNames.equals(appData.featureNames) : appData.featureNames != null)
            return false;
        if (dexHash != null ? !dexHash.equals(appData.dexHash) : appData.dexHash != null) return false;
        if (arscHash != null ? !arscHash.equals(appData.arscHash) : appData.arscHash != null) return false;
        if (drawableHashes != null ? !drawableHashes.equals(appData.drawableHashes) : appData.drawableHashes != null)
            return false;
        if (layoutHashes != null ? !layoutHashes.equals(appData.layoutHashes) : appData.layoutHashes != null)
            return false;
        if (assetHashes != null ? !assetHashes.equals(appData.assetHashes) : appData.assetHashes != null) return false;
        if (otherHashes != null ? !otherHashes.equals(appData.otherHashes) : appData.otherHashes != null) return false;
        return packageClasses != null ? packageClasses.equals(appData.packageClasses) : appData.packageClasses == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uploadTime != null ? uploadTime.hashCode() : 0);
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
        result = 31 * result + (signAlgorithm != null ? signAlgorithm.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (publicKeyMd5 != null ? publicKeyMd5.hashCode() : 0);
        result = 31 * result + (certMd5 != null ? certMd5.hashCode() : 0);
        result = 31 * result + serialNumber;
        result = 31 * result + (issuerName != null ? issuerName.hashCode() : 0);
        result = 31 * result + (issuerOrganization != null ? issuerOrganization.hashCode() : 0);
        result = 31 * result + (issuerCountry != null ? issuerCountry.hashCode() : 0);
        result = 31 * result + (subjectName != null ? subjectName.hashCode() : 0);
        result = 31 * result + (subjectOrganization != null ? subjectOrganization.hashCode() : 0);
        result = 31 * result + (subjectCountry != null ? subjectCountry.hashCode() : 0);
        result = 31 * result + numberActivities;
        result = 31 * result + activitiesAggregatedHash;
        result = 31 * result + (activityNames != null ? activityNames.hashCode() : 0);
        result = 31 * result + numberServices;
        result = 31 * result + servicesAggregatedHash;
        result = 31 * result + (serviceNames != null ? serviceNames.hashCode() : 0);
        result = 31 * result + numberContentProviders;
        result = 31 * result + contentProvidersAggregatedHash;
        result = 31 * result + (contentProviderNames != null ? contentProviderNames.hashCode() : 0);
        result = 31 * result + numberBroadcastReceivers;
        result = 31 * result + broadcastReceiversAggregatedHash;
        result = 31 * result + (broadcastReceiverNames != null ? broadcastReceiverNames.hashCode() : 0);
        result = 31 * result + numberDefinedPermissions;
        result = 31 * result + definedPermissionsAggregatedHash;
        result = 31 * result + (definedPermissions != null ? definedPermissions.hashCode() : 0);
        result = 31 * result + numberUsedPermissions;
        result = 31 * result + usedPermissionsAggregatedHash;
        result = 31 * result + (usedPermissions != null ? usedPermissions.hashCode() : 0);
        result = 31 * result + numberFeatures;
        result = 31 * result + featuresAggregatedHash;
        result = 31 * result + (featureNames != null ? featureNames.hashCode() : 0);
        result = 31 * result + (dexHash != null ? dexHash.hashCode() : 0);
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + (drawableHashes != null ? drawableHashes.hashCode() : 0);
        result = 31 * result + (layoutHashes != null ? layoutHashes.hashCode() : 0);
        result = 31 * result + (assetHashes != null ? assetHashes.hashCode() : 0);
        result = 31 * result + (otherHashes != null ? otherHashes.hashCode() : 0);
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
        result = 31 * result + pngDrawables;
        result = 31 * result + ninePatchDrawables;
        result = 31 * result + jpgDrawables;
        result = 31 * result + gifDrawables;
        result = 31 * result + xmlDrawables;
        result = 31 * result + ldpiDrawables;
        result = 31 * result + mdpiDrawables;
        result = 31 * result + hdpiDrawables;
        result = 31 * result + xhdpiDrawables;
        result = 31 * result + xxhdpiDrawables;
        result = 31 * result + xxxhdpiDrawables;
        result = 31 * result + nodpiDrawables;
        result = 31 * result + tvdpiDrawables;
        result = 31 * result + unspecifiedDpiDrawables;
        result = 31 * result + (packageClasses != null ? packageClasses.hashCode() : 0);
        result = 31 * result + packageClassesAggregatedHash;
        result = 31 * result + numberPackageClasses;
        result = 31 * result + otherClassesAggregatedHash;
        result = 31 * result + numberOtherClasses;
        return result;
    }

    @Override
    public String toString() {
        return "AppData{" +
                "id=" + id +
                ", uploadTime=" + uploadTime +
                ", androidId='" + androidId + '\'' +
                ", hash=" + hash +
                ", analysisMode=" + analysisMode +
                ", packageName='" + packageName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", source=" + source +
                ", apkSize=" + apkSize +
                ", minSdkVersion=" + minSdkVersion +
                ", targetSdkVersion=" + targetSdkVersion +
                ", signAlgorithm='" + signAlgorithm + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", publicKeyMd5='" + publicKeyMd5 + '\'' +
                ", certMd5='" + certMd5 + '\'' +
                ", serialNumber=" + serialNumber +
                ", issuerName='" + issuerName + '\'' +
                ", issuerOrganization='" + issuerOrganization + '\'' +
                ", issuerCountry='" + issuerCountry + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectOrganization='" + subjectOrganization + '\'' +
                ", subjectCountry='" + subjectCountry + '\'' +
                ", numberActivities=" + numberActivities +
                ", activitiesAggregatedHash=" + activitiesAggregatedHash +
                ", activityNames=" + activityNames +
                ", numberServices=" + numberServices +
                ", servicesAggregatedHash=" + servicesAggregatedHash +
                ", serviceNames=" + serviceNames +
                ", numberContentProviders=" + numberContentProviders +
                ", contentProvidersAggregatedHash=" + contentProvidersAggregatedHash +
                ", contentProviderNames=" + contentProviderNames +
                ", numberBroadcastReceivers=" + numberBroadcastReceivers +
                ", broadcastReceiversAggregatedHash=" + broadcastReceiversAggregatedHash +
                ", broadcastReceiverNames=" + broadcastReceiverNames +
                ", numberDefinedPermissions=" + numberDefinedPermissions +
                ", definedPermissionsAggregatedHash=" + definedPermissionsAggregatedHash +
                ", definedPermissions=" + definedPermissions +
                ", numberUsedPermissions=" + numberUsedPermissions +
                ", usedPermissionsAggregatedHash=" + usedPermissionsAggregatedHash +
                ", usedPermissions=" + usedPermissions +
                ", numberFeatures=" + numberFeatures +
                ", featuresAggregatedHash=" + featuresAggregatedHash +
                ", featureNames=" + featureNames +
                ", dexHash='" + dexHash + '\'' +
                ", arscHash='" + arscHash + '\'' +
                ", drawableHashes=" + drawableHashes +
                ", layoutHashes=" + layoutHashes +
                ", assetHashes=" + assetHashes +
                ", otherHashes=" + otherHashes +
                ", numberDrawables=" + numberDrawables +
                ", numberLayouts=" + numberLayouts +
                ", numberAssets=" + numberAssets +
                ", numberOthers=" + numberOthers +
                ", drawablesAggregatedHash=" + drawablesAggregatedHash +
                ", layoutsAggregatedHash=" + layoutsAggregatedHash +
                ", assetsAggregatedHash=" + assetsAggregatedHash +
                ", otherAggregatedHash=" + otherAggregatedHash +
                ", numberDifferentDrawables=" + numberDifferentDrawables +
                ", numberDifferentLayouts=" + numberDifferentLayouts +
                ", pngDrawables=" + pngDrawables +
                ", ninePatchDrawables=" + ninePatchDrawables +
                ", jpgDrawables=" + jpgDrawables +
                ", gifDrawables=" + gifDrawables +
                ", xmlDrawables=" + xmlDrawables +
                ", ldpiDrawables=" + ldpiDrawables +
                ", mdpiDrawables=" + mdpiDrawables +
                ", hdpiDrawables=" + hdpiDrawables +
                ", xhdpiDrawables=" + xhdpiDrawables +
                ", xxhdpiDrawables=" + xxhdpiDrawables +
                ", xxxhdpiDrawables=" + xxxhdpiDrawables +
                ", nodpiDrawables=" + nodpiDrawables +
                ", tvdpiDrawables=" + tvdpiDrawables +
                ", unspecifiedDpiDrawables=" + unspecifiedDpiDrawables +
                ", packageClasses=" + packageClasses +
                ", packageClassesAggregatedHash=" + packageClassesAggregatedHash +
                ", numberPackageClasses=" + numberPackageClasses +
                ", otherClassesAggregatedHash=" + otherClassesAggregatedHash +
                ", numberOtherClasses=" + numberOtherClasses +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public AnalysisMode getAnalysisMode() {
        return analysisMode;
    }

    public void setAnalysisMode(AnalysisMode analysisMode) {
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

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getActivitiesAggregatedHash() {
        return activitiesAggregatedHash;
    }

    public void setActivitiesAggregatedHash(int activitiesAggregatedHash) {
        this.activitiesAggregatedHash = activitiesAggregatedHash;
    }

    public int getServicesAggregatedHash() {
        return servicesAggregatedHash;
    }

    public void setServicesAggregatedHash(int servicesAggregatedHash) {
        this.servicesAggregatedHash = servicesAggregatedHash;
    }

    public int getContentProvidersAggregatedHash() {
        return contentProvidersAggregatedHash;
    }

    public void setContentProvidersAggregatedHash(int contentProvidersAggregatedHash) {
        this.contentProvidersAggregatedHash = contentProvidersAggregatedHash;
    }

    public int getBroadcastReceiversAggregatedHash() {
        return broadcastReceiversAggregatedHash;
    }

    public void setBroadcastReceiversAggregatedHash(int broadcastReceiversAggregatedHash) {
        this.broadcastReceiversAggregatedHash = broadcastReceiversAggregatedHash;
    }

    public int getDefinedPermissionsAggregatedHash() {
        return definedPermissionsAggregatedHash;
    }

    public void setDefinedPermissionsAggregatedHash(int definedPermissionsAggregatedHash) {
        this.definedPermissionsAggregatedHash = definedPermissionsAggregatedHash;
    }

    public int getUsedPermissionsAggregatedHash() {
        return usedPermissionsAggregatedHash;
    }

    public void setUsedPermissionsAggregatedHash(int usedPermissionsAggregatedHash) {
        this.usedPermissionsAggregatedHash = usedPermissionsAggregatedHash;
    }

    public int getFeaturesAggregatedHash() {
        return featuresAggregatedHash;
    }

    public void setFeaturesAggregatedHash(int featuresAggregatedHash) {
        this.featuresAggregatedHash = featuresAggregatedHash;
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

    public int getPackageClassesAggregatedHash() {
        return packageClassesAggregatedHash;
    }

    public void setPackageClassesAggregatedHash(int packageClassesAggregatedHash) {
        this.packageClassesAggregatedHash = packageClassesAggregatedHash;
    }

    public int getOtherClassesAggregatedHash() {
        return otherClassesAggregatedHash;
    }

    public void setOtherClassesAggregatedHash(int otherClassesAggregatedHash) {
        this.otherClassesAggregatedHash = otherClassesAggregatedHash;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public AppSource getSource() {
        return source;
    }

    public void setSource(AppSource source) {
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

    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    public void setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPublicKeyMd5() {
        return publicKeyMd5;
    }

    public void setPublicKeyMd5(String publicKeyMd5) {
        this.publicKeyMd5 = publicKeyMd5;
    }

    public String getCertMd5() {
        return certMd5;
    }

    public void setCertMd5(String certMd5) {
        this.certMd5 = certMd5;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssuerOrganization() {
        return issuerOrganization;
    }

    public void setIssuerOrganization(String issuerOrganization) {
        this.issuerOrganization = issuerOrganization;
    }

    public String getIssuerCountry() {
        return issuerCountry;
    }

    public void setIssuerCountry(String issuerCountry) {
        this.issuerCountry = issuerCountry;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectOrganization() {
        return subjectOrganization;
    }

    public void setSubjectOrganization(String subjectOrganization) {
        this.subjectOrganization = subjectOrganization;
    }

    public String getSubjectCountry() {
        return subjectCountry;
    }

    public void setSubjectCountry(String subjectCountry) {
        this.subjectCountry = subjectCountry;
    }

    public int getNumberActivities() {
        return numberActivities;
    }

    public void setNumberActivities(int numberActivities) {
        this.numberActivities = numberActivities;
    }

    public List<String> getActivityNames() {
        return activityNames;
    }

    public void setActivityNames(List<String> activityNames) {
        this.activityNames = activityNames;
    }

    public int getNumberServices() {
        return numberServices;
    }

    public void setNumberServices(int numberServices) {
        this.numberServices = numberServices;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public int getNumberContentProviders() {
        return numberContentProviders;
    }

    public void setNumberContentProviders(int numberContentProviders) {
        this.numberContentProviders = numberContentProviders;
    }

    public List<String> getContentProviderNames() {
        return contentProviderNames;
    }

    public void setContentProviderNames(List<String> contentProviderNames) {
        this.contentProviderNames = contentProviderNames;
    }

    public int getNumberBroadcastReceivers() {
        return numberBroadcastReceivers;
    }

    public void setNumberBroadcastReceivers(int numberBroadcastReceivers) {
        this.numberBroadcastReceivers = numberBroadcastReceivers;
    }

    public List<String> getBroadcastReceiverNames() {
        return broadcastReceiverNames;
    }

    public void setBroadcastReceiverNames(List<String> broadcastReceiverNames) {
        this.broadcastReceiverNames = broadcastReceiverNames;
    }

    public int getNumberDefinedPermissions() {
        return numberDefinedPermissions;
    }

    public void setNumberDefinedPermissions(int numberDefinedPermissions) {
        this.numberDefinedPermissions = numberDefinedPermissions;
    }

    public List<String> getDefinedPermissions() {
        return definedPermissions;
    }

    public void setDefinedPermissions(List<String> definedPermissions) {
        this.definedPermissions = definedPermissions;
    }

    public int getNumberUsedPermissions() {
        return numberUsedPermissions;
    }

    public void setNumberUsedPermissions(int numberUsedPermissions) {
        this.numberUsedPermissions = numberUsedPermissions;
    }

    public List<String> getUsedPermissions() {
        return usedPermissions;
    }

    public void setUsedPermissions(List<String> usedPermissions) {
        this.usedPermissions = usedPermissions;
    }

    public int getNumberFeatures() {
        return numberFeatures;
    }

    public void setNumberFeatures(int numberFeatures) {
        this.numberFeatures = numberFeatures;
    }

    public List<String> getFeatureNames() {
        return featureNames;
    }

    public void setFeatureNames(List<String> featureNames) {
        this.featureNames = featureNames;
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

    public List<String> getDrawableHashes() {
        return drawableHashes;
    }

    public void setDrawableHashes(List<String> drawableHashes) {
        this.drawableHashes = drawableHashes;
    }

    public List<String> getLayoutHashes() {
        return layoutHashes;
    }

    public void setLayoutHashes(List<String> layoutHashes) {
        this.layoutHashes = layoutHashes;
    }

    public List<String> getAssetHashes() {
        return assetHashes;
    }

    public void setAssetHashes(List<String> assetHashes) {
        this.assetHashes = assetHashes;
    }

    public List<String> getOtherHashes() {
        return otherHashes;
    }

    public void setOtherHashes(List<String> otherHashes) {
        this.otherHashes = otherHashes;
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

    public int getPngDrawables() {
        return pngDrawables;
    }

    public void setPngDrawables(int pngDrawables) {
        this.pngDrawables = pngDrawables;
    }

    public int getNinePatchDrawables() {
        return ninePatchDrawables;
    }

    public void setNinePatchDrawables(int ninePatchDrawables) {
        this.ninePatchDrawables = ninePatchDrawables;
    }

    public int getJpgDrawables() {
        return jpgDrawables;
    }

    public void setJpgDrawables(int jpgDrawables) {
        this.jpgDrawables = jpgDrawables;
    }

    public int getGifDrawables() {
        return gifDrawables;
    }

    public void setGifDrawables(int gifDrawables) {
        this.gifDrawables = gifDrawables;
    }

    public int getXmlDrawables() {
        return xmlDrawables;
    }

    public void setXmlDrawables(int xmlDrawables) {
        this.xmlDrawables = xmlDrawables;
    }

    public int getLdpiDrawables() {
        return ldpiDrawables;
    }

    public void setLdpiDrawables(int ldpiDrawables) {
        this.ldpiDrawables = ldpiDrawables;
    }

    public int getMdpiDrawables() {
        return mdpiDrawables;
    }

    public void setMdpiDrawables(int mdpiDrawables) {
        this.mdpiDrawables = mdpiDrawables;
    }

    public int getHdpiDrawables() {
        return hdpiDrawables;
    }

    public void setHdpiDrawables(int hdpiDrawables) {
        this.hdpiDrawables = hdpiDrawables;
    }

    public int getXhdpiDrawables() {
        return xhdpiDrawables;
    }

    public void setXhdpiDrawables(int xhdpiDrawables) {
        this.xhdpiDrawables = xhdpiDrawables;
    }

    public int getXxhdpiDrawables() {
        return xxhdpiDrawables;
    }

    public void setXxhdpiDrawables(int xxhdpiDrawables) {
        this.xxhdpiDrawables = xxhdpiDrawables;
    }

    public int getXxxhdpiDrawables() {
        return xxxhdpiDrawables;
    }

    public void setXxxhdpiDrawables(int xxxhdpiDrawables) {
        this.xxxhdpiDrawables = xxxhdpiDrawables;
    }

    public int getNodpiDrawables() {
        return nodpiDrawables;
    }

    public void setNodpiDrawables(int nodpiDrawables) {
        this.nodpiDrawables = nodpiDrawables;
    }

    public int getTvdpiDrawables() {
        return tvdpiDrawables;
    }

    public void setTvdpiDrawables(int tvdpiDrawables) {
        this.tvdpiDrawables = tvdpiDrawables;
    }

    public int getUnspecifiedDpiDrawables() {
        return unspecifiedDpiDrawables;
    }

    public void setUnspecifiedDpiDrawables(int unspecifiedDpiDrawables) {
        this.unspecifiedDpiDrawables = unspecifiedDpiDrawables;
    }

    public List<String> getPackageClasses() {
        return packageClasses;
    }

    public void setPackageClasses(List<String> packageClasses) {
        this.packageClasses = packageClasses;
    }

    public int getNumberPackageClasses() {
        return numberPackageClasses;
    }

    public void setNumberPackageClasses(int numberPackageClasses) {
        this.numberPackageClasses = numberPackageClasses;
    }

    public int getNumberOtherClasses() {
        return numberOtherClasses;
    }

    public void setNumberOtherClasses(int numberOtherClasses) {
        this.numberOtherClasses = numberOtherClasses;
    }
}