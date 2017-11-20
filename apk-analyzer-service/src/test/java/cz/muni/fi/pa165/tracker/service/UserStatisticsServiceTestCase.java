package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dao.AppDataDao;
import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.DataAccessExceptionTranslateAspect;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Testing basic functionality of {@link UserStatisticsServiceImpl}.
 *
 * @author Martin Styk
 * @version 18.11.2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserStatisticsServiceTestCase {

    @Mock
    private AppDataDao activityReportDao;

    private UserStatisticsService statisticsService;

    private User nonPersistedUser;
    private User validUser;
    private User noReportUser;

    private SportActivity hockey;
    private SportActivity football;

    private ActivityReport hockeyReport1;
    private ActivityReport footballReport1;
    private ActivityReport hockeyReport2;

    private int caloriesHockeyReport1 = 100;
    private int caloriesFootballReport2 = 1000;
    private int caloriesHockeyReport3 = 10000;

    private LocalDate searchAfterDate = LocalDate.now().minusDays(3);
    private LocalDate searchBeforeDate = LocalDate.now().minusDays(2);

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);

        // This is workaround for correct proxy object setup. We need to do it this ugly way to enable Aspect on
        // mocked object sportService
        // We can not inject services, otherwise exception translation will not work
        DataAccessExceptionTranslateAspect translateAspect = new DataAccessExceptionTranslateAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new UserStatisticsServiceImpl());
        factory.addAspect(translateAspect);

        statisticsService = factory.getProxy();
        ReflectionTestUtils.setField(statisticsService, "activityReportDao", activityReportDao);
    }

    @BeforeMethod
    public void initSportActivities() {
        nonPersistedUser = new User.Builder("non@persisted.com").build();
        validUser = new User.Builder("adam@sportman.com")
                .setDateOfBirth(LocalDate.now().minusYears(30))
                .setFirstName("Adam")
                .setSex(Sex.MALE)
                .setLastName("Sportman")
                .setHeight(180)
                .setWeight(70)
                .setRole(UserRole.REGULAR)
                .setPasswordHash("aadsadas")
                .build();

        noReportUser = new User.Builder("lenivy@nesportman.com")
                .setDateOfBirth(LocalDate.now().minusYears(30))
                .setFirstName("Lukas")
                .setSex(Sex.MALE)
                .setLastName("Lenoch")
                .setHeight(180)
                .setWeight(70)
                .setRole(UserRole.REGULAR)
                .setPasswordHash("aadsadas")
                .build();


        hockey = new SportActivity("hockey");
        hockey.setCaloriesFactor(2.5);
        hockey.setId(1L);

        football = new SportActivity("football");
        football.setCaloriesFactor(1.5);
        football.setId(2L);

        hockeyReport1 = new ActivityReport(validUser,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                hockey, caloriesHockeyReport1);

        footballReport1 = new ActivityReport(validUser,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2),
                football, caloriesFootballReport2);

        hockeyReport2 = new ActivityReport(validUser,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(1),
                hockey, caloriesHockeyReport3);

    }

    @BeforeMethod(dependsOnMethods = "initSportActivities")
    public void initMocksBehaviour() {
        //findByUser
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            User user = (User) invocation.getArguments()[0];
            if (user.equals(nonPersistedUser)) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }
            if (user.equals(noReportUser)) {
                return new ArrayList<ActivityReport>();
            }
            return Arrays.asList(hockeyReport1, footballReport1, hockeyReport2);
        }).when(activityReportDao).findReportsByUser(any(User.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalCaloriesNullUser() {
        statisticsService.getTotalCalories(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getTotalCaloriesNotPersistedUser() {
        statisticsService.getTotalCalories(nonPersistedUser);
    }

    @Test
    public void getTotalCaloriesNoReportUser() {
        assertEquals(statisticsService.getTotalCalories(noReportUser), 0);
    }

    @Test
    public void getTotalCaloriesForUser() {
        assertEquals(statisticsService.getTotalCalories(validUser), caloriesHockeyReport1 + caloriesFootballReport2 + caloriesHockeyReport3);
    }

    @Test
    public void getTotalCaloriesWhenCaloriesAreNullOnActivity() {
        footballReport1.setBurnedCalories(null);
        assertEquals(statisticsService.getTotalCalories(validUser), caloriesHockeyReport1 + caloriesHockeyReport3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalCaloriesInTimeFrameNullUser() {
        statisticsService.getTotalCalories(null, searchAfterDate, searchBeforeDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalCaloriesInTimeFrameNullStartTime() {
        statisticsService.getTotalCalories(validUser, null, searchBeforeDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalCaloriesInTimeFrameNullEndTime() {
        statisticsService.getTotalCalories(validUser, searchAfterDate, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalCaloriesInTimeFrameInvalidTimeSequence() {
        statisticsService.getTotalCalories(validUser, searchBeforeDate, searchAfterDate);
    }

    @Test
    public void getTotalCaloriesInTimeFrame() {
        assertEquals(statisticsService.getTotalCalories(validUser, searchAfterDate, searchBeforeDate),
                caloriesHockeyReport1 + caloriesFootballReport2);
    }

    @Test
    public void getTotalCaloriesInTimeFrameNoReportUser() {
        assertEquals(statisticsService.getTotalCalories(noReportUser, searchAfterDate, searchBeforeDate), 0);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getTotalCaloriesInTimeFrameNotPersistedUser() {
        statisticsService.getTotalCalories(nonPersistedUser, searchAfterDate, searchBeforeDate);
    }

    @Test
    public void getTotalCaloriesInOneDayTimeFrame() {
        assertEquals(statisticsService.getTotalCalories(validUser, searchAfterDate, searchAfterDate),
                caloriesFootballReport2);
    }

    @Test
    public void getTotalCaloriesInTimeFrameWithNoneActivities() {
        assertEquals(statisticsService.getTotalCalories(validUser, searchAfterDate.minusYears(1),
                searchBeforeDate.minusYears(1)), 0);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalActivitiesNullUser() {
        statisticsService.getTotalActivities(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getTotalActivitiesNotPersistedUser() {
        statisticsService.getTotalActivities(nonPersistedUser);
    }

    @Test
    public void getTotalActivitiesNoReportUser() {
        assertEquals(statisticsService.getTotalCalories(noReportUser), 0);
    }

    @Test
    public void getTotalActivitiesForUser() {
        assertEquals(statisticsService.getTotalActivities(validUser), 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalActivitiesInTimeFrameNullUser() {
        statisticsService.getTotalActivities(null, searchAfterDate, searchBeforeDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalActivitiesInTimeFrameNullStartTime() {
        statisticsService.getTotalActivities(validUser, null, searchBeforeDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalActivitiesInTimeFrameNullEndTime() {
        statisticsService.getTotalActivities(validUser, searchAfterDate, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getTotalActivitiesInTimeFrameInvalidTimeSequence() {
        statisticsService.getTotalActivities(validUser, searchBeforeDate, searchAfterDate);
    }

    @Test
    public void getTotalActivitiesInTimeFrame() {
        assertEquals(statisticsService.getTotalActivities(validUser, searchAfterDate, searchBeforeDate), 2);
    }

    @Test
    public void getTotalActivitiesInOneDayTimeFrame() {
        assertEquals(statisticsService.getTotalActivities(validUser, searchAfterDate, searchAfterDate), 1);
    }

    @Test
    public void getTotalActivitiesInTimeFrameWithNoneActivities() {
        assertEquals(statisticsService.getTotalActivities(validUser, searchAfterDate.minusYears(1),
                searchBeforeDate.minusYears(1)), 0);
    }

    @Test
    public void getTotalActivitiesInTimeFrameNoReportUser() {
        assertEquals(statisticsService.getTotalActivities(noReportUser, searchAfterDate, searchBeforeDate), 0);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getTotalActivitiesInTimeFrameNotPersistedUser() {
        statisticsService.getTotalActivities(nonPersistedUser, searchAfterDate, searchBeforeDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getSportsPerformedByNullUser() {
        statisticsService.getSportsPerformedByUser(null);
    }

    @Test
    public void getSportsPerformedByNoReportUser() {
        assertEquals(statisticsService.getSportsPerformedByUser(noReportUser).size(), 0);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getSportsPerformedByNotPersistedUser() {
        statisticsService.getSportsPerformedByUser(nonPersistedUser);
    }

    @Test
    public void getSportsPerformedUser() {
        Map<SportActivity, Integer> result = statisticsService.getSportsPerformedByUser(validUser);
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey(hockey));
        assertTrue(result.containsKey(football));
        assertEquals((int) result.get(hockey), 2);
        assertEquals((int) result.get(football), 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getCaloriesForSportsPerformedByNullUser() {
        statisticsService.getCaloriesForSportsOfUser(null);
    }

    @Test
    public void getCaloriesForSportsPerformedByNoReportUser() {
        assertEquals(statisticsService.getCaloriesForSportsOfUser(noReportUser).size(), 0);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void getCaloriesForSportsPerformedByNotPersistedUser() {
        statisticsService.getCaloriesForSportsOfUser(nonPersistedUser);
    }

    @Test
    public void getCaloriesForSportsPerformedUser() {
        Map<SportActivity, Integer> result = statisticsService.getCaloriesForSportsOfUser(validUser);
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey(hockey));
        assertTrue(result.containsKey(football));
        assertEquals((int) result.get(hockey), caloriesHockeyReport1 + caloriesHockeyReport3);
        assertEquals((int) result.get(football), caloriesFootballReport2);
    }


}
