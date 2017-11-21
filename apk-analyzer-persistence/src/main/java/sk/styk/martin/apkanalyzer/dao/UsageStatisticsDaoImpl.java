package sk.styk.martin.apkanalyzer.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Repository
public class UsageStatisticsDaoImpl implements UsageStatisticsDao {

    @PersistenceContext
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
}