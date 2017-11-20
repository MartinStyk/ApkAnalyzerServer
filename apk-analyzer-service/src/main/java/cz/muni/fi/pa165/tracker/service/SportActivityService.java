package cz.muni.fi.pa165.tracker.service;

import java.util.List;

/**
 * Business logic for work with SportActivity
 *
 * @author Adam Laurenčík
 * @version 12.11.2016
 */
public interface SportActivityService {

    /**
     * Creates sportActivity
     *
     * @param sportActivity to be created
     * @throws IllegalArgumentException if sportActivity is null
     */
    void create(SportActivity sportActivity);

    /**
     * Creates sportActivity
     *
     * @param sportActivity to be updated
     * @return updated sportActivity entity
     * @throws IllegalArgumentException if sportActivity is null
     */
    SportActivity update(SportActivity sportActivity);

    /**
     * Returns sportActivity entity with given id
     *
     * @param id of entity to be found
     * @return entity with given id
     */
    SportActivity findById(Long id);

    /**
     * Returns sportActivity entity with given name
     *
     * @param name of entity to be found
     * @return entity with given id
     */
    SportActivity findByName(String name);

    /**
     * Returns all sportActivity entities.
     *
     * @return list of all sportActivity entities
     */
    List<SportActivity> findAll();

    void remove(SportActivity sportActivity);

}
