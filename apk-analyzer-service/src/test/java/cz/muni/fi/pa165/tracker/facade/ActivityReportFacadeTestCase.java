/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dto.ActivityReportCreateDTO;
import cz.muni.fi.pa165.tracker.dto.ActivityReportDTO;
import cz.muni.fi.pa165.tracker.dto.ActivityReportUpdateDTO;
import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingServiceImpl;
import cz.muni.fi.pa165.tracker.service.ActivityReportService;
import cz.muni.fi.pa165.tracker.service.SportActivityService;
import cz.muni.fi.pa165.tracker.service.UserService;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Jan Grundmann
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ActivityReportFacadeTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private ActivityReportService activityReportService;

    @Mock
    private UserService userService;

    @Mock
    private SportActivityService sportActivityService;

    @Spy
    @Inject
    private final BeanMappingService beanMappingService = new BeanMappingServiceImpl();

    @InjectMocks
    private final ActivityReportFacade activityReportFacade = new ActivityReportFacadeImpl();

    @Captor
    ArgumentCaptor<ActivityReport> argumentCaptor;

    private User user;
    private User user2;
    private SportActivity hockey;
    private SportActivity football;
    private ActivityReport report1;
    private ActivityReport report2;

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);
    }

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

        report1 = new ActivityReport(1L);
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

    @BeforeMethod(dependsOnMethods = "initEntities")
    public void initMocksBehaviour() {

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findById(2L)).thenReturn(user2);

        when(sportActivityService.findById(1L)).thenReturn(hockey);
        when(sportActivityService.findById(2L)).thenReturn(football);

        // findById
        when(activityReportService.findById(0L)).thenReturn(null);
        when(activityReportService.findById(1L)).thenReturn(report1);
        when(activityReportService.findById(2L)).thenReturn(report2);

        //findBySport
        when(activityReportService.findBySport(hockey)).thenReturn(Arrays.asList(report1));
        when(activityReportService.findBySport(football)).thenReturn(Arrays.asList(report2));

        //findByUser
        when(activityReportService.findByUser(user)).thenReturn(Arrays.asList(report1));
        when(activityReportService.findByUser(user2)).thenReturn(Arrays.asList(report2));

        //findByUserAndSport
        when(activityReportService.findByUserAndSport(user, hockey)).thenReturn(Arrays.asList(report1));
        when(activityReportService.findByUserAndSport(user2, hockey)).thenReturn(Arrays.asList());
    }

    @Test
    public void testClassInitializationTest() {
        assertNotNull(activityReportService);
        assertNotNull(sportActivityService);
        assertNotNull(beanMappingService);
        assertNotNull(activityReportFacade);
        assertNotNull(userService);
    }

    @Test
    public void createActivityReportTest() {
        ActivityReportCreateDTO arcDTO = new ActivityReportCreateDTO();
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        arcDTO.setEndTime(endTime);
        arcDTO.setStartTime(startTime);
        arcDTO.setUserId(1L);
        arcDTO.setSportActivityId(2L);

        activityReportFacade.createActivityReport(arcDTO);
        verify(activityReportService).create(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getStartTime(), startTime);
        assertEquals(argumentCaptor.getValue().getEndTime(), endTime);
        assertEquals(argumentCaptor.getValue().getSportActivity(), football);
        assertEquals(argumentCaptor.getValue().getUser(), user);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void createActivityReportTestNonExistingUser() {
        ActivityReportCreateDTO arcDTO = new ActivityReportCreateDTO();
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        arcDTO.setEndTime(endTime);
        arcDTO.setStartTime(startTime);
        arcDTO.setUserId(5L);
        arcDTO.setSportActivityId(2L);

        activityReportFacade.createActivityReport(arcDTO);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void createActivityReportTestNonExistingSport() {
        ActivityReportCreateDTO arcDTO = new ActivityReportCreateDTO();
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        arcDTO.setEndTime(endTime);
        arcDTO.setStartTime(startTime);
        arcDTO.setUserId(1L);
        arcDTO.setSportActivityId(5L);

        activityReportFacade.createActivityReport(arcDTO);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullActivityReportTest() {
        activityReportFacade.createActivityReport(null);
    }

    @Test
    public void updateActivityReportTestSportActivity() {
        ActivityReportUpdateDTO updatedArDTO = new ActivityReportUpdateDTO();
        updatedArDTO.setId(1L);
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        updatedArDTO.setEndTime(endTime);
        updatedArDTO.setStartTime(startTime);
        updatedArDTO.setUserId(1L);

        updatedArDTO.setSportActivityId(1L);

        activityReportFacade.updateActivityReport(updatedArDTO);
        verify(activityReportService).update(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getStartTime(), startTime);
        assertEquals(argumentCaptor.getValue().getEndTime(), endTime);
        assertEquals(argumentCaptor.getValue().getSportActivity(), hockey);
        assertEquals(argumentCaptor.getValue().getUser(), user);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateActivityReportNonExistingSport() {
        ActivityReportUpdateDTO updatedArDTO = new ActivityReportUpdateDTO();
        updatedArDTO.setId(1L);
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        updatedArDTO.setEndTime(endTime);
        updatedArDTO.setStartTime(startTime);
        updatedArDTO.setUserId(1L);
        updatedArDTO.setSportActivityId(-1L);

        activityReportFacade.updateActivityReport(updatedArDTO);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateActivityReportNonExistingReport() {
        ActivityReportUpdateDTO updatedArDTO = new ActivityReportUpdateDTO();
        updatedArDTO.setId(-1L);
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        updatedArDTO.setEndTime(endTime);
        updatedArDTO.setStartTime(startTime);
        updatedArDTO.setUserId(1L);
        updatedArDTO.setSportActivityId(1L);

        activityReportFacade.updateActivityReport(updatedArDTO);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateActivityReportNonExistingUser() {
        ActivityReportUpdateDTO updatedArDTO = new ActivityReportUpdateDTO();
        updatedArDTO.setId(1L);
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);
        updatedArDTO.setEndTime(endTime);
        updatedArDTO.setStartTime(startTime);
        updatedArDTO.setUserId(-1L);
        updatedArDTO.setSportActivityId(1L);

        activityReportFacade.updateActivityReport(updatedArDTO);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateActivityReportNullTest() {
        activityReportFacade.updateActivityReport(null);
    }

    @Test
    public void removeActivityReportTest() {
        //removing activity with id 1 -  mock is initialized to return report1
        activityReportFacade.removeActivityReport(1L);
        verify(activityReportService).remove(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), 1);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void removeNonExistingActivityReportTest() {
        activityReportFacade.removeActivityReport(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNullActivityReportTest() {
        activityReportFacade.removeActivityReport(null);
    }

    @Test
    public void removeActivityReportByUserTest() {
        activityReportFacade.removeActivityReportsOfUser(2L);
        verify(activityReportService).removeActivityReportsOfUser(user2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNullActivityReportByUserTest() {
        activityReportFacade.removeActivityReportsOfUser(null);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void removeActivityReportByNonExistingUserTest() {
        activityReportFacade.removeActivityReportsOfUser(3L);
    }

    @Test
    public void getAllActivityReports() {
        List<ActivityReport> arList = Arrays.asList(report1, report2);
        when(activityReportService.findAll()).thenReturn(arList);

        List<ActivityReportDTO> dtoList = activityReportFacade.getAllActivityReports();

        assertEquals(dtoList.size(), 2);

        for (int i = 0; i < 2; i++) {
            ActivityReport entity = arList.get(i);
            ActivityReportDTO dto = dtoList.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getEndTime(), entity.getEndTime());
            assertEquals(dto.getStartTime(), entity.getStartTime());
            assertEquals(dto.getSportActivity().getId(), entity.getSportActivity().getId());
            assertEquals(dto.getUser().getId(), entity.getUser().getId());
        }
    }

    @Test
    public void getAllActivityReportsEmptyTest() {
        when(activityReportService.findAll()).thenReturn(new ArrayList<>());

        List<ActivityReportDTO> dtoList = activityReportFacade.getAllActivityReports();
        assertNotNull(dtoList);
        assertEquals(dtoList.size(), 0);
    }

    @Test
    public void getActivityReportByIdTest() {
        ActivityReportDTO report = activityReportFacade.getActivityReportById(1L);
        assertEquals(report.getId(), report1.getId());
        assertEquals(report.getBurnedCalories(), report1.getBurnedCalories());
        assertEquals(report.getEndTime(), report1.getEndTime());
        assertEquals(report.getStartTime(), report1.getStartTime());
        assertEquals(report.getUser().getId(), report1.getUser().getId());
        assertEquals(report.getSportActivity().getId(), report1.getSportActivity().getId());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getNonExistingActivityReportByIdTest() {
        activityReportFacade.getActivityReportById(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullActivityReportByIdTest() {
        activityReportFacade.getActivityReportById(null);
    }

    @Test
    public void getActivityReportByUserTest() {
        List<ActivityReportDTO> reportsList = activityReportFacade.getActivityReportsByUser(1L);

        assertEquals(reportsList.size(), 1);
        assertEquals(reportsList.get(0).getId(), report1.getId());
        assertEquals(reportsList.get(0).getBurnedCalories(), report1.getBurnedCalories());
        assertEquals(reportsList.get(0).getEndTime(), report1.getEndTime());
        assertEquals(reportsList.get(0).getStartTime(), report1.getStartTime());
        assertEquals(reportsList.get(0).getUser().getId(), report1.getUser().getId());
        assertEquals(reportsList.get(0).getSportActivity().getId(), report1.getSportActivity().getId());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullActivityReportByUserTest() {
        activityReportFacade.getActivityReportsByUser(null);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getActivityReportByNonExistingUserTest() {
        activityReportFacade.getActivityReportsByUser(-1L);
    }

    @Test
    public void getActivityReportBySportTest() {
        List<ActivityReportDTO> reportsList = activityReportFacade.getActivityReportsBySport(1L);

        assertEquals(reportsList.size(), 1);
        assertEquals(reportsList.get(0).getId(), report1.getId());
        assertEquals(reportsList.get(0).getBurnedCalories(), report1.getBurnedCalories());
        assertEquals(reportsList.get(0).getEndTime(), report1.getEndTime());
        assertEquals(reportsList.get(0).getStartTime(), report1.getStartTime());
        assertEquals(reportsList.get(0).getUser().getId(), report1.getUser().getId());
        assertEquals(reportsList.get(0).getSportActivity().getId(), report1.getSportActivity().getId());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getActivityReportByNonExistingSportTest() {
        activityReportFacade.getActivityReportsBySport(-1L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullActivityReportBySportTest() {
        activityReportFacade.getActivityReportsBySport(null);
    }

    @Test
    public void getActivityReportByUserAndSportTest() {
        List<ActivityReportDTO> reportsList = activityReportFacade.getActivityReportsByUserAndSport(1L, 1L);

        assertEquals(reportsList.size(), 1);
        assertEquals(reportsList.get(0).getId(), report1.getId());
        assertEquals(reportsList.get(0).getBurnedCalories(), report1.getBurnedCalories());
        assertEquals(reportsList.get(0).getEndTime(), report1.getEndTime());
        assertEquals(reportsList.get(0).getStartTime(), report1.getStartTime());
        assertEquals(reportsList.get(0).getUser().getId(), report1.getUser().getId());
        assertEquals(reportsList.get(0).getSportActivity().getId(), report1.getSportActivity().getId());
    }

    @Test
    public void getActivityReportByUserAndSportEmptyTest() {
        List<ActivityReportDTO> reportsList = activityReportFacade.getActivityReportsByUserAndSport(2L, 1L);

        assertEquals(reportsList.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getActivityReportByNullUserAndSportTest() {
        activityReportFacade.getActivityReportsByUserAndSport(null, 1L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getActivityReportByUserAndNullSportTest() {
        activityReportFacade.getActivityReportsByUserAndSport(1L, null);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getActivityReportByNonExistingUserAndSportTest() {
        activityReportFacade.getActivityReportsByUserAndSport(-1L, 1L);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getActivityReportByUserAndNonExistingSportTest() {
        activityReportFacade.getActivityReportsByUserAndSport(1L, -1L);
    }
}
