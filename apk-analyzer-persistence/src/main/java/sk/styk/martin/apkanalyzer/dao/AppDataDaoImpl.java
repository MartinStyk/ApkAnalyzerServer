package sk.styk.martin.apkanalyzer.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * @author Martin Styk
 * @version 20.11.2016
 */
@Repository
public class AppDataDaoImpl implements AppDataDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AppData create(AppData appData) {
        em.persist(appData);
        return appData;
    }

    @Override
    public AppData find(long id) {
        return em.find(AppData.class, id);
    }

    @Override
    public void remove(long id) {
        em.remove(find(id));
    }

    @Override
    public AppData update(AppData appData) {
        return em.merge(appData);
    }

    @Override
    public List<AppData> findAll() {
        TypedQuery<AppData> q = em.createQuery(
                "SELECT a FROM AppData a", AppData.class);
        return q.getResultList();
    }

    @Override
    public AppData find(String deviceId, int dataHash) {
        TypedQuery<AppData> q =
                em.createQuery("SELECT a FROM AppData a WHERE a.androidId = :deviceId AND a.hash = :hash",
                AppData.class)
                .setParameter("deviceId", deviceId)
                .setParameter("hash", dataHash);

        List<AppData> resultList = q.getResultList();

        return resultList.size() > 0 ? resultList.get(0) : null;
    }

    @Override
    public List<AppData> getForDevice(String deviceId) {
        try {
            TypedQuery<AppData> q = em.createQuery(
                    "SELECT a FROM AppData a WHERE a.androidId = :deviceId",
                    AppData.class).setParameter("deviceId", deviceId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
