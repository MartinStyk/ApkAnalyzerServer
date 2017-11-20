package cz.muni.fi.pa165.tracker.rest.controller;

import cz.muni.fi.pa165.tracker.dto.ActivityReportDTO;
import cz.muni.fi.pa165.tracker.dto.SportActivityDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.ActivityReportFacade;
import cz.muni.fi.pa165.tracker.rest.configuration.RestContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Tests for rest controller for activity report entity.
 *
 * @author Martin Styk
 * @version 1.12.2016
 */
@WebAppConfiguration
@ContextConfiguration(classes = RestContextConfiguration.class)
public class ActivityReportRestControllerTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private ActivityReportFacade activityReportFacade;

    @Inject
    @InjectMocks
    private ActivityReportRestController activityReportController;

    private MockMvc mvcMocker;


    private UserDTO hossa;
    private UserDTO gaborik;
    private SportActivityDTO hockey;
    private SportActivityDTO football;
    private ActivityReportDTO hossaReportHockey;
    private ActivityReportDTO hossaReportFootball;
    private ActivityReportDTO gaborikReportHockey;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvcMocker = standaloneSetup(activityReportController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }


    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initEntities() {
        hossa = new UserDTO();
        hossa.setId(1L);
        hossa.setPasswordHash("818181");
        hossa.setEmail("m.hoss@hawks.com");
        hossa.setFirstName("Marian");
        hossa.setHeight(185);
        hossa.setLastName("Hossa");
        hossa.setRole(UserRole.REGULAR);
        hossa.setSex(Sex.MALE);
        hossa.setWeight(85);
        hossa.setDateOfBirth(LocalDate.now().minusYears(15));
        hossa.setTotalCalories(58);
        hossa.setTeam("BlackHawks");

        gaborik = new UserDTO();
        gaborik.setId(2L);
        gaborik.setPasswordHash("101010");
        gaborik.setEmail("m.gab@kings.com");
        gaborik.setFirstName("Marian");
        gaborik.setHeight(189);
        gaborik.setLastName("Gaborik");
        gaborik.setRole(UserRole.REGULAR);
        gaborik.setSex(Sex.MALE);
        gaborik.setWeight(75);
        gaborik.setDateOfBirth(LocalDate.now().minusYears(30));
        gaborik.setTotalCalories(580);
        gaborik.setTeam("Kings");

        hockey = new SportActivityDTO();
        hockey.setName("hockey");
        hockey.setId(1L);
        hockey.setCaloriesFactor(2.5);

        football = new SportActivityDTO();
        football.setName("hockey");
        football.setId(2L);
        football.setCaloriesFactor(2.5);

        hossaReportHockey = new ActivityReportDTO();
        hossaReportHockey.setId(1L);
        hossaReportHockey.setStartTime(LocalDateTime.now().minusHours(2));
        hossaReportHockey.setEndTime(LocalDateTime.now().minusHours(1));
        hossaReportHockey.setBurnedCalories(400);
        hossaReportHockey.setSportActivity(hockey);
        hossaReportHockey.setUser(hossa);

        hossaReportFootball = new ActivityReportDTO();
        hossaReportFootball.setId(2L);
        hossaReportFootball.setStartTime(LocalDateTime.now().minusHours(2));
        hossaReportFootball.setEndTime(LocalDateTime.now().minusHours(1));
        hossaReportFootball.setBurnedCalories(4000);
        hossaReportFootball.setSportActivity(football);
        hossaReportFootball.setUser(hossa);

        gaborikReportHockey = new ActivityReportDTO();
        gaborikReportHockey.setId(3L);
        gaborikReportHockey.setStartTime(LocalDateTime.now().minusHours(2));
        gaborikReportHockey.setEndTime(LocalDateTime.now().minusHours(1));
        gaborikReportHockey.setBurnedCalories(40);
        gaborikReportHockey.setSportActivity(hockey);
        gaborikReportHockey.setUser(gaborik);
    }

    // basic configuration of find methods. specific details are handled inside of test methods
    @BeforeMethod(dependsOnMethods = "initEntities")
    public void initMocksBehaviour() {
        // findById
        when(activityReportFacade.getActivityReportById(hossaReportHockey.getId())).thenReturn(hossaReportHockey);
        when(activityReportFacade.getActivityReportById(hossaReportFootball.getId())).thenReturn(hossaReportFootball);
        when(activityReportFacade.getActivityReportById(gaborikReportHockey.getId())).thenReturn(gaborikReportHockey);

        //findBySport
        when(activityReportFacade.getActivityReportsBySport(hockey.getId()))
                .thenReturn(Arrays.asList(hossaReportHockey, gaborikReportHockey));
        when(activityReportFacade.getActivityReportsBySport(football.getId()))
                .thenReturn(Arrays.asList(hossaReportFootball));

        //findByUser
        when(activityReportFacade.getActivityReportsByUser(hossa.getId()))
                .thenReturn(Arrays.asList(hossaReportHockey, hossaReportFootball));
        when(activityReportFacade.getActivityReportsByUser(gaborik.getId()))
                .thenReturn(Arrays.asList(gaborikReportHockey));

        //findByUserAndSpory
        when(activityReportFacade.getActivityReportsByUserAndSport(hossa.getId(), football.getId()))
                .thenReturn(Arrays.asList(hossaReportFootball));
        when(activityReportFacade.getActivityReportsByUserAndSport(hossa.getId(), hockey.getId()))
                .thenReturn(Arrays.asList(hossaReportHockey));
        when(activityReportFacade.getActivityReportsByUserAndSport(gaborik.getId(), hockey.getId()))
                .thenReturn(Arrays.asList(gaborikReportHockey));
        when(activityReportFacade.getActivityReportsByUserAndSport(gaborik.getId(), football.getId()))
                .thenReturn(new ArrayList<>());

        //findAll
        when(activityReportFacade.getAllActivityReports())
                .thenReturn(Arrays.asList(hossaReportFootball, hossaReportHockey, gaborikReportHockey));
    }

    @Test
    public void getAllUsers() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].burnedCalories")
                        .value(hossaReportHockey.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].user.id")
                        .value(hossa.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].user.email")
                        .value(hossa.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].user.firstName")
                        .value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].user.lastName")
                        .value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].user.team")
                        .value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].sportActivity.id")
                        .value(hockey.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].sportActivity.name")
                        .value(hockey.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportHockey.getId() + ")].sportActivity.caloriesFactor")
                        .value(hockey.getCaloriesFactor()))

                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].burnedCalories")
                        .value(hossaReportFootball.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.id")
                        .value(hossa.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.email")
                        .value(hossa.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.firstName")
                        .value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.lastName")
                        .value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.team")
                        .value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.id")
                        .value(football.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.name")
                        .value(football.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.caloriesFactor")
                        .value(football.getCaloriesFactor()))

                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].burnedCalories")
                        .value(gaborikReportHockey.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.id")
                        .value(gaborik.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.email")
                        .value(gaborik.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.firstName")
                        .value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.lastName")
                        .value(gaborik.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.team")
                        .value(gaborik.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.id")
                        .value(hockey.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.name")
                        .value(hockey.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.caloriesFactor")
                        .value(hockey.getCaloriesFactor()));

    }

    @Test
    public void getReportById() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT + "/" + gaborikReportHockey.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(gaborikReportHockey.getId()))
                .andExpect(jsonPath("$.burnedCalories").value(gaborikReportHockey.getBurnedCalories()))
                .andExpect(jsonPath("$.user.id").value(gaborik.getId().intValue()))
                .andExpect(jsonPath("$.user.email").value(gaborik.getEmail()))
                .andExpect(jsonPath("$.user.firstName").value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.user.lastName").value(gaborik.getLastName()))
                .andExpect(jsonPath("$.user.team").value(gaborik.getTeam()))
                .andExpect(jsonPath("$.sportActivity.id").value(hockey.getId().intValue()))
                .andExpect(jsonPath("$.sportActivity.name").value(hockey.getName()))
                .andExpect(jsonPath("$.sportActivity.caloriesFactor").value(hockey.getCaloriesFactor()));

    }

    @Test
    public void getReportByInvalidID() throws Exception {

        doThrow(NonExistingEntityException.class).when(activityReportFacade).getActivityReportById(5L);

        mvcMocker.perform(get(ApiUris.USER + "/" + 5))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getReportsByUser() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT + "?userId=" + gaborik.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].burnedCalories")
                        .value(gaborikReportHockey.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.id")
                        .value(gaborik.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.email")
                        .value(gaborik.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.firstName")
                        .value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.lastName")
                        .value(gaborik.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].user.team")
                        .value(gaborik.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.id")
                        .value(hockey.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.name")
                        .value(hockey.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + gaborikReportHockey.getId() + ")].sportActivity.caloriesFactor")
                        .value(hockey.getCaloriesFactor()));
    }

    @Test
    public void getReportsByInvalidUser() throws Exception {

        doThrow(NonExistingEntityException.class).when(activityReportFacade).getActivityReportsByUser(5L);

        mvcMocker.perform(get(ApiUris.REPORT + "?userId=5"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getReportsBySport() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT + "?sportId=" + football.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].burnedCalories")
                        .value(hossaReportFootball.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.id")
                        .value(hossa.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.email")
                        .value(hossa.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.firstName")
                        .value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.lastName")
                        .value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.team")
                        .value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.id")
                        .value(football.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.name")
                        .value(football.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.caloriesFactor")
                        .value(football.getCaloriesFactor()));

    }

    @Test
    public void getReportsByInvalidSport() throws Exception {

        doThrow(NonExistingEntityException.class).when(activityReportFacade).getActivityReportsBySport(5L);

        mvcMocker.perform(get(ApiUris.REPORT + "?sportId=5"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getReportsBySportAndUser() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT + "?sportId=" + football.getId() + "&userId=" + hossa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].burnedCalories")
                        .value(hossaReportFootball.getBurnedCalories()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.id")
                        .value(hossa.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.email")
                        .value(hossa.getEmail()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.firstName")
                        .value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.lastName")
                        .value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].user.team")
                        .value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.id")
                        .value(football.getId().intValue()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.name")
                        .value(football.getName()))
                .andExpect(jsonPath("$.[?(@.id==" + hossaReportFootball.getId() + ")].sportActivity.caloriesFactor")
                        .value(football.getCaloriesFactor()));

    }

    @Test
    public void getReportsBySportAndUserEmptyResult() throws Exception {

        mvcMocker.perform(get(ApiUris.REPORT + "?sportId=" + football.getId() + "&userId=" + gaborik.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    public void getReportsByInvalidSportAndUser() throws Exception {

        doThrow(NonExistingEntityException.class).when(activityReportFacade).getActivityReportsByUserAndSport(5L, 5L);

        mvcMocker.perform(get(ApiUris.REPORT + "?sportId=5&userId=5"))
                .andExpect(status().is4xxClientError());
    }

}

