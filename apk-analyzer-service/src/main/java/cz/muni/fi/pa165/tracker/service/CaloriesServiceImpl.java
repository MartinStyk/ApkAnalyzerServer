package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerServiceException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

/**
 * Implementation of {@link CaloriesService}.
 * <p>
 * This implementation uses formula from page
 * http://www.shapesense.com/fitness-exercise/calculators/activity-based-calorie-burn-calculator.aspx
 *
 * @author Martin Styk
 * @version 08.11.2016
 */
@Service
public class CaloriesServiceImpl implements CaloriesService {

    @Override
    public int getBurnedCalories(ActivityReport activityReport) {
        if (activityReport == null) {
            throw new IllegalArgumentException("Activity report is null");
        }
        return getBurnedCalories(activityReport.getUser(), activityReport.getSportActivity(),
                Duration.between(activityReport.getStartTime(), activityReport.getEndTime()));
    }

    @Override
    public int getBurnedCalories(User user, SportActivity sportActivity, Duration duration) {
        if (user == null || sportActivity == null || duration == null) {
            throw new IllegalArgumentException("Invalid null argumets in getBurnedCalories");
        }
        if (sportActivity.getCaloriesFactor() == null) {
            throw new IllegalArgumentException("Calories factor for sport activity is null");
        }
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Duration is negative");
        }

        double userAge = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();

        double metabolicRate;
        if (user.getSex() == Sex.MALE) {
            metabolicRate = (13.75 * user.getWeight()) + (5 * user.getHeight()) - (6.76 * userAge) + 66;
        } else {
            metabolicRate = (9.56 * user.getWeight()) + (1.85 * user.getHeight()) - (4.68 * userAge) + 655;
        }

        double calorieBurn = (metabolicRate / 24) * sportActivity.getCaloriesFactor() * duration.toMinutes() / 60;

        if (calorieBurn < 0) {
            throw new ActivityTrackerServiceException("Negative calories computed for user " + user + " sport "
                    + sportActivity + " duration " + duration);
        }

        return (int) calorieBurn;
    }
}
