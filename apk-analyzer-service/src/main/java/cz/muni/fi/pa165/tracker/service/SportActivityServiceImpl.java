package cz.muni.fi.pa165.tracker.service;

import java.util.List;
import javax.inject.Inject;

import cz.muni.fi.pa165.tracker.exception.TranslatePersistenceExceptions;
import org.springframework.stereotype.Service;

/**
 * Implementation of SportActivityService
 *
 * @author Adam Laurenčík
 */
@Service
@TranslatePersistenceExceptions
public class SportActivityServiceImpl implements SportActivityService {

    @Inject
    private SportActivityDao sportActivityDao;

    @Override
    public void create(SportActivity sportActivity) {
        if (sportActivity == null) {
            throw new IllegalArgumentException("sportActivity is null");
        }
        sportActivityDao.create(sportActivity);
    }

    @Override
    public SportActivity update(SportActivity sportActivity) {
        if (sportActivity == null) {
            throw new IllegalArgumentException("sportActivity is null");
        }
        return sportActivityDao.update(sportActivity);
    }

    @Override
    public SportActivity findById(Long id) {
        return sportActivityDao.findById(id);
    }

    @Override
    public SportActivity findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        return sportActivityDao.findByName(name);
    }

    @Override
    public List<SportActivity> findAll() {
        return sportActivityDao.findAll();
    }

    @Override
    public void remove(SportActivity sportActivity) {
        if (sportActivity == null) {
            throw new IllegalArgumentException("sportActivity is null");
        }
        sportActivityDao.remove(sportActivity);
    }

}
