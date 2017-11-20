/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dto.SportActivityDTO;
import cz.muni.fi.pa165.tracker.dto.UserAuthenticateDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.dto.UserStatisticsDTO;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingServiceImpl;
import cz.muni.fi.pa165.tracker.service.TimeService;
import cz.muni.fi.pa165.tracker.service.UserService;
import cz.muni.fi.pa165.tracker.service.UserStatisticsService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Laurenčík
 *
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserFacadeTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private UserService userService;

    @Mock
    private UserStatisticsService userStatisticsService;

    @Mock
    private TimeService timeService;

    @Spy
    @Inject
    private final BeanMappingService beanMappingService = new BeanMappingServiceImpl();

    @InjectMocks
    private final UserFacade userFacade = new UserFacadeImpl();

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    User user;
    UserDTO userDTO;
    User admin;
    UserDTO adminDTO;

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initUsers() {
        user = new User(1L);
        user.setEmail("prvy@mail.com");
        user.setPasswordHash("12345hhh");
        user.setFirstName("Jozef");
        user.setHeight(150);
        user.setLastName("Novak");
        user.setRole(UserRole.REGULAR);
        user.setSex(Sex.MALE);
        user.setWeight(50);
        user.setDateOfBirth(LocalDate.ofYearDay(1990, 333));

        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setHeight(user.getHeight());
        userDTO.setLastName(user.getLastName());
        userDTO.setRole(user.getRole());
        userDTO.setSex(user.getSex());
        userDTO.setWeight(user.getWeight());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setTotalCalories(58);

        admin = new User(2L);
        admin.setEmail("admin@mail.com");
        admin.setPasswordHash("123456hhh");
        admin.setFirstName("Adminka");
        admin.setHeight(150);
        admin.setLastName("Administracna");
        admin.setRole(UserRole.ADMIN);
        admin.setSex(Sex.FEMALE);
        admin.setWeight(42);
        admin.setDateOfBirth(LocalDate.ofYearDay(1959, 150));

        adminDTO = new UserDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setPasswordHash(admin.getPasswordHash());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setHeight(admin.getHeight());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setRole(admin.getRole());
        adminDTO.setSex(admin.getSex());
        adminDTO.setWeight(user.getWeight());
        adminDTO.setDateOfBirth(admin.getDateOfBirth());
        adminDTO.setTotalCalories(72);

    }

    @BeforeMethod(dependsOnMethods = "initUsers")
    public void initMocksBehaviour() {
        // findById
        when(userService.findById(1L)).thenReturn(user);
        when(userService.findById(2L)).thenReturn(admin);
        when(userService.findById(0L)).thenReturn(null);

        //findByEmail
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(userService.findByEmail(admin.getEmail())).thenReturn(admin);
        when(userService.findByEmail("i@dont.exist")).thenReturn(null);

        when(userStatisticsService.getTotalCalories(user)).thenReturn(userDTO.getTotalCalories());
        when(userStatisticsService.getTotalCalories(admin)).thenReturn(adminDTO.getTotalCalories());

        when(userService.isAdmin(user)).thenReturn(false);
        when(userService.isAdmin(admin)).thenReturn(true);

        LocalDate now = LocalDate.ofYearDay(2016, 200);
        when(timeService.dateNow()).thenReturn(now);
        when(timeService.dateWeekAgo()).thenReturn(now.minusWeeks(1));
        when(timeService.dateMonthAgo()).thenReturn(now.minusMonths(1));
    }

    @Test
    public void testClassInitializationTest() {
        assertNotNull(userService);
        assertNotNull(beanMappingService);
        assertNotNull(userFacade);
        assertNotNull(userService);
    }

    @Test
    public void createUserTest() {
        userFacade.createUser(userDTO);
        verify(userService).registerUser(userArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue(), user);
        assertEquals(stringArgumentCaptor.getValue(), userDTO.getPasswordHash());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullUserTest() {
        userFacade.createUser(null);
    }

    @Test
    public void updateUserTest() {
        final String newName = "VydalaSomSa";
        adminDTO.setLastName(newName);
        userFacade.updateUser(adminDTO);
        verify(userService).update(userArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue().getId(), adminDTO.getId());
        assertEquals(userArgumentCaptor.getValue().getLastName(), newName);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateNonExistingUserTest() {
        userDTO.setEmail("i@dont.exist");
        userDTO.setId(0L);
        userFacade.updateUser(userDTO);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullUserTest() {
        userFacade.updateUser(null);
    }

    @Test
    public void removeUserTest() {
        userFacade.removeUser(adminDTO);
        verify(userService).deleteUser(userArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue().getId(), admin.getId());
        assertEquals(userArgumentCaptor.getValue().getEmail(), admin.getEmail());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void removeUserNonExistingTest() {
        userDTO.setId(0L);
        userFacade.removeUser(userDTO);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNullTest() {
        userFacade.removeUser(null);
    }

    @Test
    public void findUserByIdTest() {
        UserDTO userFromFacade = userFacade.findUserById(user.getId());
        assertDeepEqualsDTO(userFromFacade, userDTO);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void findNonExistingUserByIdTest() {
        userFacade.findUserById(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findNullTeamByIdTest() {
        userFacade.findUserById(null);
    }

    @Test
    public void findByNameTest() {
        UserDTO returned = userFacade.findUserByEmail(user.getEmail());
        assertDeepEqualsDTO(userDTO, returned);
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void findNonExistingTeamByNameTest() {
        userFacade.findUserByEmail("i@dont.exist");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullTeamByNameTest() {
        userFacade.findUserByEmail(null);
    }

    @Test
    public void findAllUsersTest() {
        List<User> entities = Arrays.asList(user, admin);
        when(userService.findAll()).thenReturn(entities);
        List<UserDTO> returned = userFacade.findAll();
        assertDeepEqualsDTO(returned.get(0), userDTO);
        assertDeepEqualsDTO(returned.get(1), adminDTO);
    }

    @Test
    public void getAllUsersEmptyTest() {
        when(userService.findAll()).thenReturn(new ArrayList<>());

        List<UserDTO> dtoList = userFacade.findAll();
        assertNotNull(dtoList);
        assertEquals(dtoList.size(), 0);
    }

    @Test
    public void logInTest() {
        UserAuthenticateDTO authUser = new UserAuthenticateDTO();
        authUser.setUserId(user.getId());
        authUser.setPassword("hesloPredHashovanim");
        when(userService.authenticateUser(user, "hesloPredHashovanim")).thenReturn(true);
        Assert.assertTrue(userFacade.logIn(authUser));
        UserAuthenticateDTO wrongPass = new UserAuthenticateDTO();
        wrongPass.setUserId(admin.getId());
        wrongPass.setPassword("zleHeslo");
        when(userService.authenticateUser(admin, "zleHeslo")).thenReturn(false);
        Assert.assertFalse(userFacade.logIn(wrongPass));
    }

    @Test
    public void isAdminTest() {
        Assert.assertTrue(userFacade.isAdmin(adminDTO));
        Assert.assertFalse(userFacade.isAdmin(userDTO));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void isAdminNullTest() {
        userFacade.isAdmin(null);
    }

    @Test
    public void getStatisticsTest() {
        int totalCalories = 8000;
        int lastWeekCalories = 1000;
        int lastMonthCalories = 5000;
        int totalSportActivities = 2;
        int lastWeekSportActivities = 1;
        int lastMonthSportActivities = 2;
        SportActivity hockey = new SportActivity("hockey");
        hockey.setId(1L);
        hockey.setCaloriesFactor(2.5);
        SportActivityDTO hockeyDTO = new SportActivityDTO();
        hockeyDTO.setId(hockey.getId());
        hockeyDTO.setName(hockey.getName());
        hockeyDTO.setCaloriesFactor(hockey.getCaloriesFactor());
        int hockeyCount = 20;
        int hockeyCalories = 200;

        SportActivity football = new SportActivity("football");
        football.setId(2L);
        football.setCaloriesFactor(1.01);
        SportActivityDTO footballDTO = new SportActivityDTO();
        footballDTO.setId(football.getId());
        footballDTO.setName(football.getName());
        footballDTO.setCaloriesFactor(football.getCaloriesFactor());
        int footballCount = 1;
        int footballCalories = 300;
        Map<SportActivity, Integer> sportsOfUser = new HashMap<>();
        sportsOfUser.put(hockey, hockeyCount);
        sportsOfUser.put(football, footballCount);

        Map<SportActivity, Integer> caloriesOfSports = new HashMap<>();
        caloriesOfSports.put(hockey, hockeyCalories);
        caloriesOfSports.put(football, footballCalories);

        when(userStatisticsService.getTotalCalories(user)).thenReturn(totalCalories);
        when(userStatisticsService.getTotalCalories(user, timeService.dateWeekAgo(), timeService.dateNow())).thenReturn(lastWeekCalories);
        when(userStatisticsService.getTotalCalories(user, timeService.dateMonthAgo(), timeService.dateNow())).thenReturn(lastMonthCalories);
        when(userStatisticsService.getTotalActivities(user)).thenReturn(totalSportActivities);
        when(userStatisticsService.getTotalActivities(user, timeService.dateWeekAgo(), timeService.dateNow())).thenReturn(lastWeekSportActivities);
        when(userStatisticsService.getTotalActivities(user, timeService.dateMonthAgo(), timeService.dateNow())).thenReturn(lastMonthSportActivities);
        when(userStatisticsService.getSportsPerformedByUser(user)).thenReturn(sportsOfUser);
        when(userStatisticsService.getCaloriesForSportsOfUser(user)).thenReturn(caloriesOfSports);

        UserStatisticsDTO returned = userFacade.getStatistics(userDTO);
        Assert.assertTrue(returned.getCaloriesForActivities().containsKey(hockeyDTO));
        Assert.assertTrue(returned.getCaloriesForActivities().containsKey(footballDTO));
        Assert.assertEquals(returned.getCaloriesForActivities().get(hockeyDTO).intValue(), hockeyCalories);
        Assert.assertEquals(returned.getCaloriesForActivities().get(footballDTO).intValue(), footballCalories);
        Assert.assertTrue(returned.getSportActivities().containsKey(hockeyDTO));
        Assert.assertTrue(returned.getSportActivities().containsKey(footballDTO));
        Assert.assertEquals(returned.getSportActivities().get(hockeyDTO).intValue(), hockeyCount);
        Assert.assertEquals(returned.getSportActivities().get(footballDTO).intValue(), footballCount);
        Assert.assertEquals(returned.getCaloriesLastMonth(), lastMonthCalories);
        Assert.assertEquals(returned.getCaloriesLastWeek(), lastWeekCalories);
        Assert.assertEquals(returned.getTotalCalories(), totalCalories);
        Assert.assertEquals(returned.getTotalSportActivities(), totalSportActivities);
        Assert.assertEquals(returned.getSportActivitiesLastMonth(), lastMonthSportActivities);
        Assert.assertEquals(returned.getSportActivitiesLastWeek(), lastWeekSportActivities);

    }

    private void assertDeepEqualsDTO(UserDTO user1, UserDTO user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getDateOfBirth(), user2.getDateOfBirth());
        assertEquals(user1.getPasswordHash(), user2.getPasswordHash());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getRole(), user2.getRole());
        assertEquals(user1.getSex(), user2.getSex());
        assertEquals(user1.getTeam(), user2.getTeam());
    }

}
