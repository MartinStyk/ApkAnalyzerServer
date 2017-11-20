/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dao.AppDataDao;
import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.DataAccessExceptionTranslateAspect;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.verify;

import org.testng.Assert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

/**
 * @author Jan Grundmann
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ActivityReportServiceTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private AppDataDao arDao;

    private ActivityReportService arService;

    @Captor
    ArgumentCaptor<ActivityReport> argumentCaptor;

    private User user;
    private User user2;
    private SportActivity hockey;
    private SportActivity football;
    private ActivityReport report1;
    private ActivityReport report2;

    private final long createdEntityId = 123L;
    private final long updatedEntityId = 154L;
    private final long alreadyExistingEntityId = 2L;
    private final long notPersistedEntityId = 666L;

    @BeforeMethod
    public void initEntities() {
        user = new User(1L);
        user.setEmail("pepa@mail.com");
        user.setPasswordHash("12345");
        user.setFirstName("Pepa");
        user.setHeight(150);
        user.setLastName("Novy");
        user.setRole(UserRole.REGULAR);
        user.setSex(Sex.MALE);
        user.setWeight(50);
        user.setDateOfBirth(LocalDate.ofYearDay(1990, 333));

        user2 = new User(2L);
        user2.setEmail("josef@mail.com");
        user2.setPasswordHash("heslo");
        user2.setFirstName("Josef");
        user2.setHeight(150);
        user2.setLastName("Stary");
        user2.setRole(UserRole.REGULAR);
        user2.setSex(Sex.MALE);
        user2.setWeight(80);
        user2.setDateOfBirth(LocalDate.ofYearDay(1991, 333));

        hockey = new SportActivity("hockey");
        hockey.setId(1L);
        hockey.setCaloriesFactor(2.5);

        football = new SportActivity("football");
        football.setId(2L);
        football.setCaloriesFactor(1.5);

        report1 = new ActivityReport();
        report1.setStartTime(LocalDateTime.now().minusHours(2));
        report1.setEndTime(LocalDateTime.now().minusHours(1));
        report1.setBurnedCalories(400);
        report1.setSportActivity(hockey);
        report1.setUser(user);

        report2 = new ActivityReport(2L);
        report2.setStartTime(LocalDateTime.now().minusHours(4));
        report2.setEndTime(LocalDateTime.now().minusHours(2));
        report2.setBurnedCalories(500);
        report2.setSportActivity(football);
        report2.setUser(user2);
    }

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);

        // This is workaround for correct proxy object setup. We need to do it this ugly way to enable Aspect on
        // mocked object sportService
        // We can not inject services, otherwise exception translation will not work
        DataAccessExceptionTranslateAspect translateAspect = new DataAccessExceptionTranslateAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new ActivityReportServiceImpl());
        factory.addAspect(translateAspect);

        arService = factory.getProxy();
        ReflectionTestUtils.setField(arService, "activityReportDao", arDao);
        ReflectionTestUtils.setField(arService, "caloriesService", new CaloriesServiceImpl());
    }

    @BeforeMethod(dependsOnMethods = "initEntities")
    public void initMocksBehaviour() {

        // findById
        when(arDao.findActivityReportByID(0L)).thenReturn(null);
        when(arDao.findActivityReportByID(2L)).thenReturn(report2);

        //findBySport
        when(arDao.findReportsBySportActivity(football)).thenReturn(Arrays.asList(report2));

        //findByUser
        when(arDao.findReportsByUser(user2)).thenReturn(Arrays.asList(report2));

        //create
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            ActivityReport activityReport = (ActivityReport) invocation.getArguments()[0];

            if (activityReport.getId() != null && activityReport.getId().equals(alreadyExistingEntityId)) {
                throw new EntityExistsException("This is behaviour of EntityManager");
            }

            if (activityReport.getSportActivity() == null
                    || activityReport.getUser() == null
                    || activityReport.getBurnedCalories() == null
                    || activityReport.getEndTime() == null
                    || activityReport.getStartTime() == null) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            activityReport.setId(createdEntityId);
            return null; //this is happy day scenario
        }).when(arDao).create(any(ActivityReport.class));

        //update
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            ActivityReport activityReport = (ActivityReport) invocation.getArguments()[0];

            if (activityReport.getSportActivity() == null
                    || activityReport.getUser() == null
                    || activityReport.getBurnedCalories() == null
                    || activityReport.getEndTime() == null
                    || activityReport.getStartTime() == null) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            if (activityReport.getId() == null) {
                activityReport.setId(updatedEntityId);//safe
            }

            return activityReport; //this is happy day scenario
        }).when(arDao).update(any(ActivityReport.class));

        //remove
        doAnswer((InvocationOnMock invocation) -> {
            Object argument = invocation.getArguments()[0];
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            ActivityReport activityReport = (ActivityReport) invocation.getArguments()[0];

            if (activityReport.getId() == alreadyExistingEntityId) //happy day scenario
                return null;

            if (activityReport.getId() == notPersistedEntityId) //entity is not saved
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");

            return null;
        }).when(arDao).delete(any(ActivityReport.class));
    }

    @Test
    public void create() {
        arService.create(report1);
        verify(arDao).create(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), report1);
        assertNotNull(report1);
        assertEquals((long) report1.getId(), createdEntityId);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNull() {
        arService.create(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createWithNullUser() {
        report1.setUser(null);
        arService.create(report1);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createWithNullSport() {
        report1.setSportActivity(null);
        arService.create(report1);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createExisting() {
        report1.setId(alreadyExistingEntityId);
        arService.create(report1);
    }

    @Test
    public void update() {
        assertNotNull(report2.getId());
        ActivityReport updated = arService.update(report2);
        verify(arDao).update(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), report2);
        assertEquals(updated.getId(), report2.getId());
        assertEquals(updated.getEndTime(), report2.getEndTime());
        assertEquals(updated.getStartTime(), report2.getStartTime());
        assertEquals(updated.getBurnedCalories(), report2.getBurnedCalories());
        assertEquals(updated.getSportActivity().getId(), report2.getSportActivity().getId());
        assertEquals(updated.getUser().getId(), report2.getUser().getId());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNull() {
        arService.update(null);
    }

    @Test(enabled = false)
    public void updateNonExisting() {
        assertNull(report1.getId());
        ActivityReport updated = arService.update(report1);
        assertDeepEquals(updated, report1);
    }

    @Test
    public void findById() {
        ActivityReport found = arService.findById(2L);
        assertDeepEquals(found, report2);
    }

    @Test
    public void findNotExistingById() {
        assertNull(arService.findById(0L));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findByIdNull() {
        arService.findById(null);
    }

    @Test
    public void findBySport() {
        List<ActivityReport> found = arService.findBySport(football);
        assertDeepEquals(found.get(0), report2);
    }

    @Test
    public void findNotExistingBySport() {
        List<ActivityReport> found = arService.findBySport(new SportActivity());
        assertEquals(found.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findBySportNull() {
        arService.findBySport(null);
    }

    @Test
    public void findByUser() {
        List<ActivityReport> found = arService.findByUser(user2);
        assertDeepEquals(found.get(0), report2);
    }

    @Test
    public void findNotExistingByUser() {
        List<ActivityReport> found = arService.findByUser(new User());
        assertEquals(found.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findByUserNull() {
        arService.findByUser(null);
    }

    @Test
    public void findByUserAndSport() {
        List<ActivityReport> found = arService.findByUserAndSport(user2, football);
        assertDeepEquals(found.get(0), report2);
    }

    @Test
    public void findNotExistingByUserAndSport() {
        List<ActivityReport> found = arService.findByUserAndSport(new User(), new SportActivity());
        assertEquals(found.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findByUserAndSportNull() {
        arService.findByUserAndSport(null, null);
    }


    @Test
    public void remove() {
        report2.setId(alreadyExistingEntityId);
        arService.remove(report2);
        verify(arDao).delete(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), report2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNull() {
        arService.remove(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void removeNonExisting() {
        report2.setId(notPersistedEntityId);
        arService.remove(report2);
    }

    @Test
    public void findAllNonEmptyResult() {
        List<ActivityReport> entityList = Arrays.asList(report1, report2);
        when(arDao.findAll()).thenReturn(entityList);

        List<ActivityReport> resultList = arService.findAll();

        assertEquals(resultList.size(), entityList.size());

        //just to check no modification of persisted data is done on this layer
        for (int i = 0; i < entityList.size(); i++) {
            ActivityReport entity = entityList.get(i);
            ActivityReport result = resultList.get(i);
            assertDeepEquals(result, entity);
        }
    }

    @Test
    public void findAllEmptyResult() {
        when(arDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(arService.findAll().size(), 0);
    }

    @Test
    public void removeReportsOfUser() {
        arService.removeActivityReportsOfUser(user);
        verify(arDao).deleteUserReports(user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeReportsOfUserNull() {
        arService.removeActivityReportsOfUser(null);
    }

    private void assertDeepEquals(ActivityReport report1, ActivityReport report2) {
        Assert.assertEquals(report1.getId(), report2.getId());
        Assert.assertEquals(report1.getEndTime(), report2.getEndTime());
        Assert.assertEquals(report1.getStartTime(), report2.getStartTime());
        Assert.assertEquals(report1.getBurnedCalories(), report2.getBurnedCalories());
        Assert.assertEquals(report1.getSportActivity().getId(), report2.getSportActivity().getId());
        Assert.assertEquals(report1.getUser().getId(), report2.getUser().getId());
    }
}
