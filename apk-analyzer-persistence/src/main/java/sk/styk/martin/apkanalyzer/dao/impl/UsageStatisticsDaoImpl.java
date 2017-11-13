package sk.styk.martin.apkanalyzer.dao.impl;

import sk.styk.martin.apkanalyzer.dao.UsageStatisticsDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * @author mstyk
 * @date 11/13/17
 */
@ApplicationScoped
@Transactional(value = Transactional.TxType.REQUIRED)
public class UsageStatisticsDaoImpl implements UsageStatisticsDao {

    @Inject
    private EntityManager em;

    @Override
    public long numberOfUploadedApps() {
        Query query = em.createQuery("SELECT count(*) FROM AppData data");
        return (long) query.getSingleResult();
    }

    @Override
    public long numberOfDistinctUploadedApps() {
        Query query = em.createQuery("SELECT count(Distinct data.hash) FROM AppData data");
        return (long) query.getSingleResult();
    }

    @Override
    public long numberOfDistinctDevices() {
        Query query = em.createQuery("SELECT count(Distinct data.androidId) FROM AppData data");
        return (long) query.getSingleResult();
    }

    @Override
    public long uploadsByDevice(String androidId) {
        Query q = em.createQuery("SELECT count(*) FROM AppData a WHERE a.androidId = :deviceId")
              .setParameter("deviceId", androidId);
        return (long) q.getSingleResult();
    }

    @Override
    public long totalFileHashes() {
        return 1;
//        Query drawableQuery = em.createQuery("SELECT count(*) FROM DrawableFiles files ");
//        Query layoutQuery = em.createQuery("SELECT count(*) FROM LayoutFiles files ");
//        Query assetQuery = em.createQuery("SELECT count(*) FROM AssetFiles files ");
//        Query otherQuery = em.createQuery("SELECT count(*) FROM OtherFiles files ");
//        return (long) drawableQuery.getSingleResult() + (long) layoutQuery.getSingleResult()
//              +(long) assetQuery.getSingleResult() + (long) otherQuery.getSingleResult();
    }
}
