/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.dto.SportActivityCreateDTO;
import cz.muni.fi.pa165.tracker.dto.SportActivityDTO;
import cz.muni.fi.pa165.tracker.dto.SportActivityUpdateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.service.SportActivityService;

import java.util.List;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of (@link SportActivityFacade}
 *
 * @author Adam Laurenčík
 * @version 13.11.2016
 */
@Service
@Transactional
public class SportActivityFacadeImpl implements SportActivityFacade {

    @Inject
    private SportActivityService sportActivityService;

    @Inject
    private BeanMappingService beanMappingService;

    @Override
    public Long createSportActivity(SportActivityCreateDTO sportActivity) {
        if (sportActivity == null) {
            throw new IllegalArgumentException("sportActivity cannot be null");
        }
        SportActivity sportActivityEntity = beanMappingService.mapTo(sportActivity, SportActivity.class);
        sportActivityService.create(sportActivityEntity);
        return sportActivityEntity.getId();
    }

    @Override
    public void updateSportActivity(SportActivityUpdateDTO sportActivity) {
        if (sportActivity == null) {
            throw new IllegalArgumentException("sportActivity cannot be null");
        }
        if (sportActivityService.findById(sportActivity.getId()) == null) {
            throw new NonExistingEntityException("Can not update non existing sport activity");
        }
        SportActivity sportActivityEntity = beanMappingService.mapTo(sportActivity, SportActivity.class);
        sportActivityService.update(sportActivityEntity);
    }

    @Override
    public void removeSportActivity(Long sportActivityId) {
        if (sportActivityId == null) {
            throw new IllegalArgumentException("sportActivityId is null");
        }
        SportActivity sportActivity = sportActivityService.findById(sportActivityId);
        if (sportActivity == null) {
            throw new NonExistingEntityException("Cannot remove not existing sportActivity");
        }
        sportActivityService.remove(sportActivity);
    }

    @Override
    public SportActivityDTO getSportActivityById(Long sportActivityId) {
        if (sportActivityId == null) {
            throw new IllegalArgumentException("sportActivityId is null");
        }
        SportActivity sportActivity = sportActivityService.findById(sportActivityId);
        if (sportActivity == null) {
            throw new NonExistingEntityException("SportActivity for given id does not exists");
        }
        return beanMappingService.mapTo(sportActivity, SportActivityDTO.class);
    }

    @Override
    public SportActivityDTO getSportActivityByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        SportActivity sportActivity = sportActivityService.findByName(name);
        if (sportActivity == null) {
            throw new NonExistingEntityException("SportActivity for given name does not exists");
        }
        return beanMappingService.mapTo(sportActivity, SportActivityDTO.class);
    }

    @Override
    public List<SportActivityDTO> getAllSportActivities() {
        return beanMappingService.mapTo(sportActivityService.findAll(), SportActivityDTO.class);
    }

}
