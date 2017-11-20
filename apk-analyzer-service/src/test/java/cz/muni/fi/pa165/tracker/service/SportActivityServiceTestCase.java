package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.DataAccessExceptionTranslateAspect;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Martin Styk
 * @version 17.11.2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class SportActivityServiceTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private SportActivityDao sportActivityDao;

    private SportActivityService sportService;

    @Captor
    ArgumentCaptor<SportActivity> argumentCaptor;

    private SportActivity hockey;
    private SportActivity footballPersisted;
    private SportActivity cyclingPersisted;

    private long createdEntityId = 99L;
    private long updatedEntityId = 100L;
    private long alreadyExistingEntityId = 77L;
    private long notPersistedEntityId = 78L;
    private String alreadyExistingSportName = "alreadyExistingSportName";

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);

        // This is workaround for correct proxy object setup. We need to do it this ugly way to enable Aspect on
        // mocked object sportService
        // We can not inject services, otherwise exception translation will not work
        DataAccessExceptionTranslateAspect translateAspect = new DataAccessExceptionTranslateAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new SportActivityServiceImpl());
        factory.addAspect(translateAspect);

        sportService = factory.getProxy();
        ReflectionTestUtils.setField(sportService, "sportActivityDao", sportActivityDao);
    }

    @BeforeMethod
    public void initSportActivities() {
        hockey = new SportActivity("hockey");
        hockey.setCaloriesFactor(2.5);

        footballPersisted = new SportActivity("football");
        footballPersisted.setCaloriesFactor(1.5);
        footballPersisted.setId(50L);

        cyclingPersisted = new SportActivity("cycling");
        cyclingPersisted.setCaloriesFactor(5d);
        cyclingPersisted.setId(51L);
    }

    @BeforeMethod(dependsOnMethods = "initSportActivities")
    public void initMocksBehaviour() {
        //findByName
        when(sportActivityDao.findByName("hockey")).thenReturn(hockey);
        when(sportActivityDao.findByName("non existing")).thenReturn(null);

        // findById
        when(sportActivityDao.findById(0L)).thenReturn(null);
        when(sportActivityDao.findById(1L)).thenReturn(cyclingPersisted);
        when(sportActivityDao.findById(2L)).thenReturn(footballPersisted);

        doAnswer((InvocationOnMock invocation) -> {
            throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
        }).when(sportActivityDao).findById(null);

        //create
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            SportActivity sportActivity = (SportActivity) invocation.getArguments()[0];

            if (sportActivity.getId() != null && sportActivity.getId().equals(alreadyExistingEntityId)) {
                throw new EntityExistsException("This is behaviour of EntityManager");
            }

            if (sportActivity.getName() == null
                    || sportActivity.getName().equals(alreadyExistingSportName)
                    || sportActivity.getCaloriesFactor() == null
                    || sportActivity.getCaloriesFactor() < 1) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            sportActivity.setId(createdEntityId);
            return null; //this is happy day scenario
        }).when(sportActivityDao).create(any(SportActivity.class));


        //update
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            SportActivity sportActivity = (SportActivity) invocation.getArguments()[0];

            if (sportActivity.getName() == null
                    || sportActivity.getName().equals(alreadyExistingSportName)
                    || sportActivity.getCaloriesFactor() == null
                    || sportActivity.getCaloriesFactor() < 1) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            if (sportActivity.getId() == null) {
                sportActivity.setId(updatedEntityId);//safe
            }

            return sportActivity; //this is happy day scenario
        }).when(sportActivityDao).update(any(SportActivity.class));

        //remove
        doAnswer((InvocationOnMock invocation) -> {
            Object argument = invocation.getArguments()[0];
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            SportActivity sportActivity = (SportActivity) invocation.getArguments()[0];

            if (sportActivity.getId() == alreadyExistingEntityId) //happy day scenario
                return null;

            if (sportActivity.getId() == notPersistedEntityId) //entity is not saved
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");

            return null;
        }).when(sportActivityDao).remove(any(SportActivity.class));
    }

    @Test
    public void create() {
        sportService.create(hockey);
        verify(sportActivityDao).create(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), hockey);
        assertNotNull(hockey);
        assertEquals((long) hockey.getId(), createdEntityId);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNull() {
        sportService.create(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createExisting() {
        hockey.setId(alreadyExistingEntityId);
        sportService.create(hockey);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createNameNotUnique() {
        hockey.setName(alreadyExistingSportName);
        sportService.create(hockey);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createNameNull() {
        sportService.create(new SportActivity());
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createInvalidCaloriesFactor() {
        hockey.setCaloriesFactor(-5d);
        sportService.create(hockey);
    }

    @Test
    public void update() {
        assertNotNull(footballPersisted.getId());
        SportActivity updated = sportService.update(footballPersisted);
        verify(sportActivityDao).update(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), footballPersisted);
        assertEquals(updated.getId(), footballPersisted.getId());
        assertEquals(updated.getName(), footballPersisted.getName());
        assertEquals(updated.getCaloriesFactor(), footballPersisted.getCaloriesFactor());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNull() {
        sportService.update(null);
    }

    @Test
    public void updateNonExisting() {
        assertNull(hockey.getId());
        SportActivity updated = sportService.update(hockey);
        assertDeepEquals(updated, hockey);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void updateNotUniqueName() {
        footballPersisted.setName(alreadyExistingSportName);
        sportService.update(footballPersisted);
    }

    @Test
    public void findById() {
        SportActivity found = sportService.findById(1L);
        assertDeepEquals(found, cyclingPersisted);
    }

    @Test
    public void findNotExistingById() {
        assertNull(sportService.findById(0L));
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void findByIdNull() {
        sportService.findById(null);
    }

    @Test
    public void findByName() {
        SportActivity result = sportService.findByName("hockey");
        assertDeepEquals(result, hockey);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findByNameNull() {
        sportService.findByName(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findByNameEmpty() {
        sportService.findByName("");
    }

    @Test
    public void findNotExistingByName() {
        assertNull(sportService.findByName("non existing"));
    }

    @Test
    public void findAllNonEmptyResult() {
        List<SportActivity> entityList = Arrays.asList(footballPersisted, cyclingPersisted);
        when(sportActivityDao.findAll()).thenReturn(entityList);

        List<SportActivity> resultList = sportService.findAll();

        assertEquals(resultList.size(), entityList.size());

        //just to check no modification of persisted data is done on this layer
        for (int i = 0; i < entityList.size(); i++) {
            SportActivity entity = entityList.get(i);
            SportActivity result = resultList.get(i);
            assertDeepEquals(result, entity);
        }
    }

    @Test
    public void findAllEmptyResult() {
        when(sportActivityDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(sportService.findAll().size(), 0);
    }

    @Test
    public void remove() {
        footballPersisted.setId(alreadyExistingEntityId);
        sportService.remove(footballPersisted);
        verify(sportActivityDao).remove(argumentCaptor.capture());
        assertDeepEquals(argumentCaptor.getValue(), footballPersisted);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNull() {
        sportService.remove(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void removeNonExisting() {
        hockey.setId(notPersistedEntityId);
        sportService.remove(hockey);
    }

    private void assertDeepEquals(SportActivity sport1, SportActivity sport2) {
        Assert.assertEquals(sport1.getId(), sport2.getId());
        Assert.assertEquals(sport1.getName(), sport2.getName());
        Assert.assertEquals(sport1.getCaloriesFactor(), sport2.getCaloriesFactor());
    }


}