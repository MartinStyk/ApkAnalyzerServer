package cz.muni.fi.pa165.tracker.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * Business logic for statistics of user.
 *
 * @author Martin Styk
 * @version 12.11.2016
 */
public interface UserStatisticsService {

    /**
     * Computes total calories burnt by user
     *
     * @param user for whom calories are computed
     * @return total calories
     * @throws IllegalArgumentException if user is null
     */
    int getTotalCalories(User user);

    /**
     * Computes total calories burnt by user in specified time frame.
     * <p>
     * Time frame is specified with activity report start time.
     *
     * @param user       for whom calories are computed
     * @param afterDate  find calories burnt after this date, inclusive
     * @param beforeDate find calories burnt before this date, inclusive
     * @return total calories in specified time frame
     * @throws IllegalArgumentException if params are null on in incorrect order
     */
    int getTotalCalories(User user, LocalDate afterDate, LocalDate beforeDate);

    /**
     * Computes total sport activities of user all time.
     *
     * @param user for whom actiities are computed
     * @return total number of sport performances of user
     * @throws IllegalArgumentException if user is null
     */
    int getTotalActivities(User user);

    /**
     * Computes total activities by user in specified time frame.
     * <p>
     * Time frame is specified with activity report start time.
     *
     * @param user       for whom activities are computed
     * @param afterDate  find activities after this date, inclusive
     * @param beforeDate find activities before this date, inclusive
     * @return total activities in specified time frame
     * @throws IllegalArgumentException if params are null on in incorrect order
     */
    int getTotalActivities(User user, LocalDate afterDate, LocalDate beforeDate);

    /**
     * Computes which sports user perform most.
     *
     * @param user for whom statistics are computed
     * @return map in format SportActivity - number of reports with this activity for given user
     */
    Map<SportActivity, Integer> getSportsPerformedByUser(User user);

    /**
     * Computes chart of sport activity - calories burnt performing this sport.
     *
     * @param user for whom statistics are computed
     * @return map in format SportActivity - number of calories burnt during performint this sport for given user
     */
    Map<SportActivity, Integer> getCaloriesForSportsOfUser(User user);

}


