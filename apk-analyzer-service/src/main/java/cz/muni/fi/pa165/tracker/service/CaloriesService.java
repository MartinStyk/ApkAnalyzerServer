package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerServiceException;

import java.time.Duration;

/**
 * Service for computing calories burned during sport activity.
 *
 * @author Martin Styk
 * @version 07.11.2016
 */
public interface CaloriesService {

    /**
     * Computes burnt calories.
     *
     * @param user          user for whom calories are computed
     * @param sportActivity activity for which calories are computed
     * @param duration      time of practicing sport activity.
     * @return calories burnt
     * @throws IllegalArgumentException        if some of required attributes is not set or duration is negative
     * @throws ActivityTrackerServiceException if calories are negative
     */
    int getBurnedCalories(User user, SportActivity sportActivity, Duration duration);

    /**
     * Computes burnt calories.
     *
     * @param activityReport activity report for which calories are computed.
     *                       It must contain all neccessary properties.
     * @return calories burnt
     * @throws IllegalArgumentException        if some of required attributes is not set
     * @throws ActivityTrackerServiceException if calories are negative
     */
    int getBurnedCalories(ActivityReport activityReport);
}
