package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simple unit tests for {@link CaloriesServiceImpl}.
 *
 * @author Martin Styk
 * @version 17.11.2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class CaloriesServiceTestCase extends AbstractTestNGSpringContextTests {

    @Inject
    private CaloriesService caloriesService;

    private User user;
    private SportActivity sportActivity;
    private ActivityReport activityReport;

    @BeforeMethod
    public void initSportActivities() {
        user = new User.Builder("adam@sportman.com")
                .setDateOfBirth(LocalDate.now().minusYears(30))
                .setFirstName("Adam")
                .setSex(Sex.MALE)
                .setLastName("Sportman")
                .setHeight(180)
                .setWeight(70)
                .setRole(UserRole.REGULAR)
                .setPasswordHash("aadsadas")
                .build();

        sportActivity = new SportActivity("football");
        sportActivity.setCaloriesFactor(1.5);

        activityReport = new ActivityReport();
        activityReport.setSportActivity(sportActivity);
        activityReport.setUser(user);
        activityReport.setEndTime(LocalDateTime.now().minusHours(2));
        activityReport.setStartTime(LocalDateTime.now().minusHours(3));
    }


    @Test
    public void testCorrectParameter() {
        //just blackbox - expect no exceptions
        int result = caloriesService.getBurnedCalories(activityReport);
        Assert.assertTrue(result > 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullReport() {
        caloriesService.getBurnedCalories(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUserNull() {
        activityReport.setUser(null);
        caloriesService.getBurnedCalories(activityReport);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSportNull() {
        activityReport.setSportActivity(null);
        caloriesService.getBurnedCalories(activityReport);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrongDuration() {
        activityReport.setStartTime(LocalDateTime.now());
        activityReport.setEndTime(LocalDateTime.MIN);
        caloriesService.getBurnedCalories(activityReport);
    }

    @Test
    public void testDurationIsReflected() {
        activityReport.setEndTime(LocalDateTime.now().minusHours(2));
        activityReport.setStartTime(LocalDateTime.now().minusHours(3));
        int oneHourCalories = caloriesService.getBurnedCalories(activityReport);

        activityReport.setEndTime(LocalDateTime.now().minusHours(1));
        activityReport.setStartTime(LocalDateTime.now().minusHours(3));
        int twoHoursCalories = caloriesService.getBurnedCalories(activityReport);

        Assert.assertTrue(oneHourCalories < twoHoursCalories);
    }

    @Test
    public void testSexIsReflected() {
        user.setSex(Sex.MALE);
        int maleCalories = caloriesService.getBurnedCalories(activityReport);

        user.setSex(Sex.FEMALE);
        int femaleCalories = caloriesService.getBurnedCalories(activityReport);

        Assert.assertTrue(femaleCalories != maleCalories);
    }

    @Test
    public void testWeightIsReflected() {
        user.setWeight(100);
        int fatCalories = caloriesService.getBurnedCalories(activityReport);

        user.setWeight(60);
        int slimCalories = caloriesService.getBurnedCalories(activityReport);

        Assert.assertTrue(slimCalories < fatCalories);
    }

    @Test
    public void testHeightIsReflected() {
        user.setHeight(180);
        int smallCalories = caloriesService.getBurnedCalories(activityReport);

        user.setWeight(200);
        int bigCalories = caloriesService.getBurnedCalories(activityReport);

        Assert.assertTrue(smallCalories < bigCalories);
    }

    @Test
    public void testSportIsReflected() {
        sportActivity.setCaloriesFactor(10d);
        int smallCalories = caloriesService.getBurnedCalories(activityReport);

        sportActivity.setCaloriesFactor(15d);
        int bigCalories = caloriesService.getBurnedCalories(activityReport);

        Assert.assertTrue(smallCalories < bigCalories);
    }
}
