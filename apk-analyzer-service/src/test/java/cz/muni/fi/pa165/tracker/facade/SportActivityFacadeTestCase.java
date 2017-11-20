package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dto.SportActivityCreateDTO;
import cz.muni.fi.pa165.tracker.dto.SportActivityDTO;
import cz.muni.fi.pa165.tracker.dto.SportActivityUpdateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingServiceImpl;
import cz.muni.fi.pa165.tracker.service.SportActivityService;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 * @author Martin Styk
 * @version 17.11.2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class SportActivityFacadeTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private SportActivityService sportActivityService;

    @Spy
    @Inject
    private final BeanMappingService beanMappingService = new BeanMappingServiceImpl();

    @InjectMocks
    private final SportActivityFacade sportFacade = new SportActivityFacadeImpl();

    @Captor
    ArgumentCaptor<SportActivity> argumentCaptor;

    private SportActivity hockey;
    private SportActivity football;
    private SportActivity cycling;

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initSportActivities() {
        hockey = new SportActivity("hockey");
        hockey.setId(1L);
        hockey.setCaloriesFactor(2.5);

        football = new SportActivity("football");
        football.setId(2L);
        football.setCaloriesFactor(1.5);

        cycling = new SportActivity("cycling");
        cycling.setId(3L);
        cycling.setCaloriesFactor(5d);
    }

    @BeforeMethod(dependsOnMethods = "initSportActivities")
    public void initMocksBehaviour() {
        // findById
        when(sportActivityService.findById(0L)).thenReturn(null);
        when(sportActivityService.findById(1L)).thenReturn(hockey);
        when(sportActivityService.findById(2L)).thenReturn(football);
        when(sportActivityService.findById(3L)).thenReturn(cycling);

        //findByName
        when(sportActivityService.findByName("hockey")).thenReturn(hockey);
        when(sportActivityService.findByName("non existing")).thenReturn(null);
    }

    @Test
    public void testClassInitializationTest() {
        assertNotNull(sportActivityService);
        assertNotNull(beanMappingService);
        assertNotNull(sportFacade);
    }

    @Test
    public void createSportTest() {
        final String name = "New Sport";
        final double calories = 3d;
        SportActivityCreateDTO dto = new SportActivityCreateDTO();
        dto.setName(name);
        dto.setCaloriesFactor(calories);

        sportFacade.createSportActivity(dto);
        verify(sportActivityService).create(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getName(), name);
        assertEquals(argumentCaptor.getValue().getCaloriesFactor(), calories);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullSportTest() {
        sportFacade.createSportActivity(null);
    }

    @Test
    public void updateSportTest() {
        final long id = 1;
        final String name = "Updated Sport";
        final double calories = 3d;
        SportActivityUpdateDTO dto = new SportActivityUpdateDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setCaloriesFactor(calories);

        sportFacade.updateSportActivity(dto);
        verify(sportActivityService).update(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), id);
        assertEquals(argumentCaptor.getValue().getName(), name);
        assertEquals(argumentCaptor.getValue().getCaloriesFactor(), calories);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateNonExistingSportTest() {
        final long id = 0; //mock configured to return null as when sport is not found in db
        final String name = "Updated Sport";
        final double calories = 3d;
        SportActivityUpdateDTO dto = new SportActivityUpdateDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setCaloriesFactor(calories);

        sportFacade.updateSportActivity(dto);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullSportTest() {
        sportFacade.updateSportActivity(null);
    }

    @Test
    public void removeTest() {
        //removing activity with id 1 -  mock is initialized to return hockey
        sportFacade.removeSportActivity(1L);
        verify(sportActivityService).remove(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), 1);
        assertEquals(argumentCaptor.getValue().getName(), hockey.getName());
        assertEquals(argumentCaptor.getValue().getCaloriesFactor(), hockey.getCaloriesFactor());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void removeNonExistingTest() {
        //removing activity with id 0 -  mock is initialized to return null - not found
        sportFacade.removeSportActivity(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNullTest() {
        sportFacade.removeSportActivity(null);
    }

    @Test
    public void getSportByIdTest() {
        SportActivityDTO sport = sportFacade.getSportActivityById(1L);
        assertEquals(sport.getId(), hockey.getId());
        assertEquals(sport.getName(), hockey.getName());
        assertEquals(sport.getCaloriesFactor(), hockey.getCaloriesFactor());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getNonExistingSportByIdTest() {
        sportFacade.getSportActivityById(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullSportByIdTest() {
        sportFacade.getSportActivityById(null);
    }

    @Test
    public void getByNameTest() {
        SportActivityDTO sport = sportFacade.getSportActivityByName("hockey");
        assertEquals(sport.getId(), hockey.getId());
        assertEquals(sport.getName(), hockey.getName());
        assertEquals(sport.getCaloriesFactor(), hockey.getCaloriesFactor());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getNonExistingByNameTest() {
        sportFacade.getSportActivityByName("non existing");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullSportByNameTest() {
        sportFacade.getSportActivityByName(null);
    }

    @Test
    public void getAllSportsTest() {
        List<SportActivity> entityList = Arrays.asList(hockey, football, cycling);
        when(sportActivityService.findAll()).thenReturn(entityList);

        List<SportActivityDTO> dtoList = sportFacade.getAllSportActivities();

        assertEquals(dtoList.size(), 3);

        for (int i = 0; i < 3; i++) {
            SportActivity entity = entityList.get(i);
            SportActivityDTO dto = dtoList.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getName(), entity.getName());
            assertEquals(dto.getCaloriesFactor(), entity.getCaloriesFactor());
        }
    }

    @Test
    public void getAllSportsEmptyTest() {
        when(sportActivityService.findAll()).thenReturn(new ArrayList<>());

        List<SportActivityDTO> dtoList = sportFacade.getAllSportActivities();
        assertNotNull(dtoList);
        assertEquals(dtoList.size(), 0);
    }


}
