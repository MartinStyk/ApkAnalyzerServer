package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dao.AppDataDao;
import org.mockito.Mock;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.DataAccessExceptionTranslateAspect;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for UserService
 *
 * @author Adam Laurenčík
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserServiceTestCase extends AbstractTestNGSpringContextTests {

    @Mock
    private UserDao userDao;

    @Mock
    private AppDataDao activityReportDao;

    private UserService userService;

    private final long createdEntityId = 123L;
    private final long updatedEntityId = 154L;
    private final long alreadyExistingEntityId = 1L;
    private final long notPersistedEntityId = 666L;
    private final String alreadyExsistingEmail = "already@existing.mail";

    @Captor
    ArgumentCaptor<User> argumentCaptor;

    private User user;

    private User admin;

    private User withoutId;

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

        withoutId = new User();
        withoutId.setEmail("eeemail@eeemail.com");
        withoutId.setPasswordHash("12345hhh");
        withoutId.setFirstName("Jozef");
        withoutId.setHeight(150);
        withoutId.setLastName("Novak");
        withoutId.setRole(UserRole.REGULAR);
        withoutId.setSex(Sex.MALE);
        withoutId.setWeight(50);
        withoutId.setDateOfBirth(LocalDate.ofYearDay(1990, 333));

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
    }

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);

        // This is workaround for correct proxy object setup. We need to do it this ugly way to enable Aspect on
        // mocked object userService
        // We can not inject services, otherwise exception translation will not work
        DataAccessExceptionTranslateAspect translateAspect = new DataAccessExceptionTranslateAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new UserServiceImpl());
        factory.addAspect(translateAspect);

        userService = factory.getProxy();
        ReflectionTestUtils.setField(userService, "userDao", userDao);
        ReflectionTestUtils.setField(userService, "activityReportDao", activityReportDao);
    }

    @BeforeMethod(dependsOnMethods = "initUsers")
    public void initMocksBehaviour() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(user);
        when(userDao.findByEmail(admin.getEmail())).thenReturn(admin);
        when(userDao.findByEmail("i@dont.exist")).thenReturn(null);

        when(userDao.findById(user.getId())).thenReturn(user);
        when(userDao.findById(admin.getId())).thenReturn(admin);
        when(userDao.findById(0L)).thenReturn(null);

        //create
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            User u = (User) invocation.getArguments()[0];

            if (u.getId() != null && u.getId().equals(alreadyExistingEntityId)) {
                throw new EntityExistsException("This is behaviour of EntityManager");
            }

            if (u.getEmail() == null
                    || u.getEmail().equals(alreadyExsistingEmail)
                    || u.getPasswordHash() == null
                    || u.getFirstName() == null
                    || u.getLastName() == null
                    || u.getDateOfBirth() == null
                    || u.getRole() == null
                    || u.getSex() == null) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            u.setId(createdEntityId);
            return null; //this is happy day scenario
        }).when(userDao).create(any(User.class));

        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            User u = (User) invocation.getArguments()[0];

            if (u.getEmail() == null
                    || u.getEmail().equals(alreadyExsistingEmail)
                    || u.getPasswordHash() == null
                    || u.getFirstName() == null
                    || u.getLastName() == null
                    || u.getDateOfBirth() == null
                    || u.getRole() == null
                    || u.getSex() == null) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            if (u.getId() == null) {
                u.setId(updatedEntityId);//safe
            }

            return u; //this is happy day scenario
        }).when(userDao).update(any(User.class));

        //remove
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            User u = (User) invocation.getArguments()[0];

            if (u.getId() == alreadyExistingEntityId) //happy day scenario
            {
                return null;
            }

            if (u.getId() == notPersistedEntityId) //entity is not saved
            {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            return null;
        }).when(userDao).remove(any(User.class));
    }

    @Test
    public void registerUser() {
        userService.registerUser(withoutId, withoutId.getPasswordHash());
        Assert.assertNotNull(withoutId);
        assertEquals((long) withoutId.getId(), createdEntityId);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void RegisterUserNull() {
        userService.registerUser(null, "abc");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void registerUserNullPassword() {
        userService.registerUser(withoutId, null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void registerUserExistingId() {
        withoutId.setId(alreadyExistingEntityId);
        userService.registerUser(withoutId, withoutId.getPasswordHash());
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void registerUserWithExistingEmail() {
        withoutId.setEmail(alreadyExsistingEmail);
        userService.registerUser(withoutId, "abcd");
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamNullParameters() {
        userService.registerUser(new User(), "abcd");
    }

    @Test
    public void updateUser() {
        assertNotNull(user.getId());
        User updated = userService.update(user);
        verify(userDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), user);
        assertEquals(updated.getId(), user.getId());
        assertDeepEqualsWithoutId(updated, user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateUserNull() {
        userService.update(null);
    }

    @Test
    public void updatUserNonExisting() {
        assertNull(withoutId.getId());
        User updated = userService.update(withoutId);
        verify(userDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), withoutId);
        assertEquals((long) updated.getId(), updatedEntityId);
        assertDeepEqualsWithoutId(updated, withoutId);
    }

    @Test
    public void updateChangeEmail() {
        assertNotNull(user.getId());
        user.setEmail("changed@email.com");
        User updated = userService.update(user);
        verify(userDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), user);
        assertEquals(updated.getId(), user.getId());
        assertDeepEqualsWithoutId(updated, user);
    }

    @Test
    public void findAllNonEmptyResult() {

        List<User> entityList = Arrays.asList(user, admin);
        when(userDao.findAll()).thenReturn(entityList);

        List<User> resultList = userService.findAll();

        assertEquals(resultList.size(), entityList.size());

        //just to check no modification of persisted data is done on this layer
        for (int i = 0; i < entityList.size(); i++) {
            User entity = entityList.get(i);
            User result = resultList.get(i);
            assertEquals(result.getId(), entity.getId());
            assertDeepEqualsWithoutId(result, entity);
        }
    }

    @Test
    public void findAllEmptyResult() {
        when(userDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(userDao.findAll().size(), 0);
    }

    @Test
    public void findUserById() {
        User found = userService.findById(user.getId());
        assertEquals(found.getId(), user.getId());
        assertDeepEqualsWithoutId(found, user);
    }

    @Test
    public void findUserNotExistingById() {
        assertNull(userService.findById(0L));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findUserByIdNull() {
        userService.findById(null);
    }

    @Test
    public void findUserByEmail() {
        User result = userService.findByEmail(user.getEmail());
        assertEquals(result.getId(), user.getId());
        assertDeepEqualsWithoutId(result, user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findUserByEmailNull() {
        userService.findByEmail(null);
    }

    @Test
    public void findUserNonExistingByEmail() {
        assertNull(userService.findByEmail("i@dont.exist"));
    }

    @Test
    public void deleteUserTest() {
        userService.deleteUser(user);
        verify(userDao, atLeast(1)).remove(argumentCaptor.capture());
        verify(activityReportDao).deleteUserReports(user);
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNull() {
        userService.deleteUser(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNonExisting() {
        userService.deleteUser(withoutId);
    }

    @Test
    public void isAdmin() {
        Assert.assertTrue(userService.isAdmin(admin));
        Assert.assertFalse(userService.isAdmin(user));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void isAdminNull() {
        userService.isAdmin(null);
    }

    @Test
    public void authenticate() {
        userService.registerUser(withoutId, "superTajneHeslo");
        Assert.assertTrue(userService.authenticateUser(withoutId, "superTajneHeslo"));
        Assert.assertFalse(userService.authenticateUser(withoutId, "zleHeslo"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void authenticateNullUser() {
        userService.registerUser(null, "superTajneHeslo");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void authenticateNullPassword() {
        userService.registerUser(withoutId, null);
    }

    private void assertDeepEqualsWithoutId(User user1, User user2) {
        assertEquals(user1.getDateOfBirth(), user2.getDateOfBirth());
        assertEquals(user1.getPasswordHash(), user2.getPasswordHash());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getHeight(), user2.getHeight());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getRole(), user2.getRole());
        assertEquals(user1.getSex(), user2.getSex());
        assertEquals(user1.getTeam(), user2.getTeam());
        assertEquals(user1.getWeight(), user2.getWeight());
    }

}
