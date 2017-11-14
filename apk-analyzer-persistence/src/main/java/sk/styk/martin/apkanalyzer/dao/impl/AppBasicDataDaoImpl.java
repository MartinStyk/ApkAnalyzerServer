package sk.styk.martin.apkanalyzer.dao.impl;

import sk.styk.martin.apkanalyzer.dao.AppBasicDataDao;
import sk.styk.martin.apkanalyzer.dao.generic.impl.GenericQueryableDaoImpl;
import sk.styk.martin.apkanalyzer.entity.AppBasicData;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
@Transactional(value = Transactional.TxType.REQUIRED)
public class AppBasicDataDaoImpl extends GenericQueryableDaoImpl<AppBasicData, Long> implements AppBasicDataDao, Serializable {

    public AppBasicDataDaoImpl() {
        super(AppBasicData.class);
    }


    @Override
    public AppBasicData find(Long id) {
        TypedQuery<AppBasicData> q = em.createQuery("SELECT " +
              "new  sk.styk.martin.apkanalyzer.entity.AppBasicData(a.id, a.androidId, a.hash," +
              " a.analysisMode, a.packageName, a.applicationName, " +
              "a.versionName,a.versionCode, a.source, a.apkSize, " +
              "a.minSdkVersion, a.targetSdkVersion, a.certMd5, a.numberActivities, " +
              "a.activitiesAggregatedHash, a.numberServices, a.servicesAggregatedHash, " +
              "a.numberContentProviders, a.contentProvidersAggregatedHash, a.numberBroadcastReceivers," +
              "a.broadcastReceiversAggregatedHash,a.numberDefinedPermissions,a.definedPermissionsAggregatedHash," +
              "a.numberUsedPermissions, a.usedPermissionsAggregatedHash,a.numberFeatures, a.featuresAggregatedHash," +
              "a.dexHash, a.arscHash, a.numberDrawables, a.numberLayouts,a.numberAssets, a.numberOthers, " +
              "a.drawablesAggregatedHash, a.layoutsAggregatedHash, a.assetsAggregatedHash, a.otherAggregatedHash, " +
              "a.numberDifferentDrawables, a.numberDifferentLayouts,a.packageClassesAggregatedHash, " +
              "a.numberPackageClasses,a.otherClassesAggregatedHash,a.numberOtherClasses)" +
              " FROM AppData a WHERE a.id = :id", AppBasicData.class)
              .setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<AppBasicData> findAll() {
        TypedQuery<AppBasicData> q = em.createQuery("SELECT " +
              "new sk.styk.martin.apkanalyzer.entity.AppBasicData(a.id, a.androidId, a.hash," +
              " a.analysisMode, a.packageName, a.applicationName, " +
              "a.versionName,a.versionCode, a.source, a.apkSize, " +
              "a.minSdkVersion, a.targetSdkVersion, a.certMd5, a.numberActivities, " +
              "a.activitiesAggregatedHash, a.numberServices, a.servicesAggregatedHash, " +
              "a.numberContentProviders, a.contentProvidersAggregatedHash, a.numberBroadcastReceivers," +
              "a.broadcastReceiversAggregatedHash,a.numberDefinedPermissions,a.definedPermissionsAggregatedHash," +
              "a.numberUsedPermissions, a.usedPermissionsAggregatedHash,a.numberFeatures, a.featuresAggregatedHash," +
              "a.dexHash, a.arscHash, a.numberDrawables, a.numberLayouts,a.numberAssets, a.numberOthers, " +
              "a.drawablesAggregatedHash, a.layoutsAggregatedHash, a.assetsAggregatedHash, a.otherAggregatedHash, " +
              "a.numberDifferentDrawables, a.numberDifferentLayouts,a.packageClassesAggregatedHash, " +
              "a.numberPackageClasses,a.otherClassesAggregatedHash,a.numberOtherClasses)" +
              " FROM AppData a", AppBasicData.class);

        return q.getResultList();
    }

    @Override
    public List<AppBasicData> getForDevice(String deviceId) {
        TypedQuery<AppBasicData> q = em.createQuery("SELECT " +
              "new sk.styk.martin.apkanalyzer.entity.AppBasicData(a.id, a.androidId, a.hash," +
              " a.analysisMode, a.packageName, a.applicationName, " +
              "a.versionName,a.versionCode, a.source, a.apkSize, " +
              "a.minSdkVersion, a.targetSdkVersion, a.certMd5, a.numberActivities, " +
              "a.activitiesAggregatedHash, a.numberServices, a.servicesAggregatedHash, " +
              "a.numberContentProviders, a.contentProvidersAggregatedHash, a.numberBroadcastReceivers," +
              "a.broadcastReceiversAggregatedHash,a.numberDefinedPermissions,a.definedPermissionsAggregatedHash," +
              "a.numberUsedPermissions, a.usedPermissionsAggregatedHash,a.numberFeatures, a.featuresAggregatedHash," +
              "a.dexHash, a.arscHash, a.numberDrawables, a.numberLayouts,a.numberAssets, a.numberOthers, " +
              "a.drawablesAggregatedHash, a.layoutsAggregatedHash, a.assetsAggregatedHash, a.otherAggregatedHash, " +
              "a.numberDifferentDrawables, a.numberDifferentLayouts,a.packageClassesAggregatedHash, " +
              "a.numberPackageClasses,a.otherClassesAggregatedHash,a.numberOtherClasses)" +
              " FROM AppData a WHERE a.androidId = :deviceId", AppBasicData.class)
              .setParameter("deviceId", deviceId);

        return q.getResultList();
    }
}
