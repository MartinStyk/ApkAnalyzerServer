package cz.muni.fi.pa165.tracker.rest.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.UserCreateDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.TeamFacade;
import cz.muni.fi.pa165.tracker.facade.UserFacade;
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
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Tests for rest controller for user entity.
 *
 * @author Martin Styk
 * @version 1.12.2016
 */
@WebAppConfiguration
@ContextConfiguration(classes = RestContextConfiguration.class)
public class UserRestControllerTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private UserFacade userFacade;

    @Mock
    private TeamFacade teamFacade;

    @Inject
    @InjectMocks
    private UserRestController userController;

    private MockMvc mvcMocker;

    private UserDTO hossa;
    private UserDTO gaborik;
    private UserDTO nonExisting;

    private String emailHossa = "m.hoss@hawks.com";
    private String emailGaborik = "m.gab@kings.com";
    private String emailNonExisting = "i@dont.exist";

    private TeamDTO blackHawks;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvcMocker = standaloneSetup(userController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @BeforeMethod
    public void initUsers() {
        hossa = new UserDTO();
        hossa.setId(1L);
        hossa.setPasswordHash("818181");
        hossa.setEmail(emailHossa);
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
        gaborik.setEmail(emailGaborik);
        gaborik.setFirstName("Marian");
        gaborik.setHeight(189);
        gaborik.setLastName("Gaborik");
        gaborik.setRole(UserRole.REGULAR);
        gaborik.setSex(Sex.MALE);
        gaborik.setWeight(75);
        gaborik.setDateOfBirth(LocalDate.now().minusYears(30));
        gaborik.setTotalCalories(580);
        gaborik.setTeam("Kings");

        nonExisting = new UserDTO();
        nonExisting.setId(0L);
        nonExisting.setEmail(emailNonExisting);

        blackHawks = new TeamDTO();
        blackHawks.setName("BlackHawks");
        blackHawks.setId(10L);
        blackHawks.setTeamLeader(hossa);
        blackHawks.setMembers(Arrays.asList(hossa));

    }

    // basic configuration of find methods. specific details are handled inside of test methods
    @BeforeMethod(dependsOnMethods = "initUsers")
    public void initMocksBehaviour() {
        // findById
        when(userFacade.findUserById(hossa.getId())).thenReturn(hossa);
        when(userFacade.findUserById(gaborik.getId())).thenReturn(gaborik);

        //findByEmail
        when(userFacade.findUserByEmail(emailHossa)).thenReturn(hossa);
        when(userFacade.findUserByEmail(emailGaborik)).thenReturn(gaborik);

        //findAll
        when(userFacade.findAll()).thenReturn(Arrays.asList(hossa, gaborik));

        //find team of user
        when(teamFacade.getTeamByName("BlackHawks")).thenReturn(blackHawks);
    }

    @Test
    public void getAllUsers() throws Exception {

        mvcMocker.perform(get(ApiUris.USER))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].email").value(emailHossa))
                .andExpect(jsonPath("$.[?(@.id==1)].firstName").value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==1)].lastName").value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==1)].team").value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==1)].height").value(hossa.getHeight()))
                .andExpect(jsonPath("$.[?(@.id==1)].weight").value(hossa.getWeight()))
                .andExpect(jsonPath("$.[?(@.id==1)].totalCalories").value(hossa.getTotalCalories()))
                .andExpect(jsonPath("$.[?(@.id==2)].email").value(emailGaborik))
                .andExpect(jsonPath("$.[?(@.id==2)].firstName").value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==2)].lastName").value(gaborik.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==2)].team").value(gaborik.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==2)].height").value(gaborik.getHeight()))
                .andExpect(jsonPath("$.[?(@.id==2)].weight").value(gaborik.getWeight()))
                .andExpect(jsonPath("$.[?(@.id==2)].totalCalories").value(gaborik.getTotalCalories()));
    }

    @Test
    public void getUserById() throws Exception {

        mvcMocker.perform(get(ApiUris.USER + "/" + hossa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.email").value(emailHossa))
                .andExpect(jsonPath("$.firstName").value(hossa.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(hossa.getLastName()))
                .andExpect(jsonPath("$.team").value(hossa.getTeam()))
                .andExpect(jsonPath("$.height").value(hossa.getHeight()))
                .andExpect(jsonPath("$.weight").value(hossa.getWeight()))
                .andExpect(jsonPath("$.totalCalories").value(hossa.getTotalCalories()));

        mvcMocker.perform(get(ApiUris.USER + "/" + gaborik.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.email").value(emailGaborik))
                .andExpect(jsonPath("$.firstName").value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(gaborik.getLastName()))
                .andExpect(jsonPath("$.team").value(gaborik.getTeam()))
                .andExpect(jsonPath("$.height").value(gaborik.getHeight()))
                .andExpect(jsonPath("$.weight").value(gaborik.getWeight()))
                .andExpect(jsonPath("$.totalCalories").value(gaborik.getTotalCalories()));
    }

    @Test
    public void getUsersByEmail() throws Exception {

        mvcMocker.perform(get(ApiUris.USER + "?email=" + emailHossa))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].email").value(emailHossa))
                .andExpect(jsonPath("$.[?(@.id==1)].firstName").value(hossa.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==1)].lastName").value(hossa.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==1)].team").value(hossa.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==1)].height").value(hossa.getHeight()))
                .andExpect(jsonPath("$.[?(@.id==1)].weight").value(hossa.getWeight()))
                .andExpect(jsonPath("$.[?(@.id==1)].totalCalories").value(hossa.getTotalCalories()));

        mvcMocker.perform(get(ApiUris.USER + "?email=" + emailGaborik))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.id==2)].email").value(emailGaborik))
                .andExpect(jsonPath("$.[?(@.id==2)].firstName").value(gaborik.getFirstName()))
                .andExpect(jsonPath("$.[?(@.id==2)].lastName").value(gaborik.getLastName()))
                .andExpect(jsonPath("$.[?(@.id==2)].team").value(gaborik.getTeam()))
                .andExpect(jsonPath("$.[?(@.id==2)].height").value(gaborik.getHeight()))
                .andExpect(jsonPath("$.[?(@.id==2)].weight").value(gaborik.getWeight()))
                .andExpect(jsonPath("$.[?(@.id==2)].totalCalories").value(gaborik.getTotalCalories()));
    }

    @Test
    public void findTeamOfUser() throws Exception {

        mvcMocker.perform(get(ApiUris.USER + "/" + hossa.getId() + "/team"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(blackHawks.getName()))
                .andExpect(jsonPath("$.id").value(blackHawks.getId()))
                .andExpect(jsonPath("$.teamLeader.email").value(hossa.getEmail()))
                .andExpect(jsonPath("$.members.[?(@.id==1)].email").value(hossa.getEmail()));
    }


    @Test
    public void getInvalidUser() throws Exception {
        when(userFacade.findUserById(nonExisting.getId())).thenThrow(new NonExistingEntityException("I dont exist"));

        mvcMocker.perform(get(ApiUris.USER + "/" + nonExisting.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userFacade).removeUser(new UserDTO(hossa.getId()));

        mvcMocker.perform(delete(ApiUris.USER + "/" + hossa.getId()))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteNonExistingUser() throws Exception {
        doThrow(new NonExistingEntityException("I dont exist")).when(userFacade).removeUser(new UserDTO(nonExisting.getId()));

        mvcMocker.perform(delete(ApiUris.USER + "/" + nonExisting.getId()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void createUser() throws Exception {

        UserCreateDTO tatarCreate = new UserCreateDTO();
        tatarCreate.setPasswordHash("212121");
        tatarCreate.setEmail("t.tatar.@wings.us");
        tatarCreate.setFirstName("Tomáš");
        tatarCreate.setHeight(179);
        tatarCreate.setLastName("Tatar");
        tatarCreate.setRole(UserRole.REGULAR);
        tatarCreate.setSex(Sex.MALE);
        tatarCreate.setWeight(65);

        UserDTO tatar = new UserDTO();
        tatar.setPasswordHash("212121");
        tatar.setEmail("t.tatar.@wings.us");
        tatar.setFirstName("Tomáš");
        tatar.setHeight(179);
        tatar.setLastName("Tatar");
        tatar.setRole(UserRole.REGULAR);
        tatar.setSex(Sex.MALE);
        tatar.setWeight(65);

        doReturn(30L).when(userFacade).createUser(tatarCreate);
        doReturn(tatar).when(userFacade).findUserById(Long.valueOf(30));

        mvcMocker.perform(
                post(ApiUris.USER).contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(tatarCreate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(tatar.getEmail()))
                .andExpect(jsonPath("$.firstName").value(tatar.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(tatar.getLastName()))
                .andExpect(jsonPath("$.team").value(tatar.getTeam()))
                .andExpect(jsonPath("$.height").value(tatar.getHeight()))
                .andExpect(jsonPath("$.weight").value(tatar.getWeight()))
                .andExpect(jsonPath("$.totalCalories").value(tatar.getTotalCalories()));
    }

    @Test
    public void createIncompleteUser() throws Exception {

        UserCreateDTO tatarCreate = new UserCreateDTO();
        tatarCreate.setEmail("tom.tatar.@wings.us");

        doThrow(new ActivityTrackerDataAccessException("validation failed")).when(userFacade).createUser(tatarCreate);

        String json = convertObjectToJsonBytes(tatarCreate);

        mvcMocker.perform(
                post(ApiUris.USER + "/create").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void updateUser() throws Exception {

        hossa.setLastName("HossaUpdated");
        hossa.setDateOfBirth(null);

        String json = this.convertObjectToJsonBytes(hossa);

        mvcMocker.perform(
                put(ApiUris.USER + "/" + hossa.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(emailHossa))
                .andExpect(jsonPath("$.firstName").value(hossa.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(hossa.getLastName()))
                .andExpect(jsonPath("$.team").value(hossa.getTeam()))
                .andExpect(jsonPath("$.height").value(hossa.getHeight()))
                .andExpect(jsonPath("$.weight").value(hossa.getWeight()))
                .andExpect(jsonPath("$.totalCalories").value(hossa.getTotalCalories()));

    }

    @Test
    public void updateNonExistingUser() throws Exception {

        doThrow(new NonExistingEntityException("I dont exist")).when(userFacade).findUserById(nonExisting.getId());

        String json = this.convertObjectToJsonBytes(nonExisting);

        mvcMocker.perform(
                put(ApiUris.USER + "/" + nonExisting.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().is4xxClientError());

    }

    private static String convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }
}

