package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Test class for TeamService
 *
 * @author Petra Ondřejková
 * @version 21.11. 2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class TeamServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    private TeamDao teamDao;

    @Mock
    private UserService userService;

    private TeamService teamService;

    @Captor
    ArgumentCaptor<Team> argumentCaptor;

    private Team newTeam;
    private Team teamPersisted;
    private Team teamPersisted2;
    private User leader;
    private User leader2;
    private User member;

    private final long createdEntityId = 123L;
    private final long updatedEntityId = 154L;
    private final long alreadyExistingEntityId = 111L;
    private final long notPersistedEntityId = 666L;
    private final String alreadyExistingTeamName = "alreadyExistingTeamName";

    @BeforeMethod
    public void initTeams() {
        leader = new User(1L);
        leader.setEmail("pepa@mail.com");
        leader.setPasswordHash("12345");
        leader.setFirstName("Pepa");
        leader.setHeight(150);
        leader.setLastName("Novy");
        leader.setRole(UserRole.REGULAR);
        leader.setSex(Sex.MALE);
        leader.setWeight(50);
        leader.setDateOfBirth(LocalDate.ofYearDay(1990, 333));

        leader2 = new User(5L);
        leader2.setEmail("pavel@mail.com");
        leader2.setPasswordHash("totojeheslo");
        leader2.setFirstName("Pavel");
        leader2.setHeight(192);
        leader2.setLastName("Vedoucí");
        leader2.setRole(UserRole.REGULAR);
        leader2.setSex(Sex.MALE);
        leader2.setWeight(87);
        leader2.setDateOfBirth(LocalDate.ofYearDay(1990, 200));

        member = new User(2L);
        member.setEmail("clen@mail.com");
        member.setPasswordHash("heslo");
        member.setFirstName("Antonin");
        member.setHeight(164);
        member.setLastName("Novotný");
        member.setRole(UserRole.REGULAR);
        member.setSex(Sex.MALE);
        member.setWeight(65);
        member.setDateOfBirth(LocalDate.ofYearDay(1991, 222));

        newTeam = new Team("New Team");
        newTeam.setTeamLeader(leader);
        newTeam.addMember(leader);

        teamPersisted = new Team("frajeri");
        teamPersisted.setId(51L);
        teamPersisted.setTeamLeader(leader);
        teamPersisted.addMember(leader);
        teamPersisted.addMember(member);

        teamPersisted2 = new Team("frajeriNaDruhou");
        teamPersisted2.setId(7L);
        teamPersisted2.setTeamLeader(leader2);
        teamPersisted2.addMember(leader);
    }

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);

        // This is workaround for correct proxy object setup. We need to do it this ugly way to enable Aspect on
        // mocked object sportService
        // We can not inject services, otherwise exception translation will not work
        DataAccessExceptionTranslateAspect translateAspect = new DataAccessExceptionTranslateAspect();
        AspectJProxyFactory factory = new AspectJProxyFactory(new TeamServiceImpl());
        factory.addAspect(translateAspect);

        teamService = factory.getProxy();
        ReflectionTestUtils.setField(teamService, "teamDao", teamDao);
        ReflectionTestUtils.setField(teamService, "userService", userService);
    }

    @BeforeMethod(dependsOnMethods = "initTeams")
    public void initMocksBehaviour() {

        //findByName
        when(teamDao.findByName("frajeri")).thenReturn(teamPersisted);
        when(teamDao.findByName("non existing")).thenReturn(null);

        // findById
        when(teamDao.findById(0L)).thenReturn(null);
        when(teamDao.findById(1L)).thenReturn(teamPersisted);

        doAnswer((InvocationOnMock invocation) -> {
            throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
        }).when(teamDao).findById(null);

        //create
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            Team team = (Team) invocation.getArguments()[0];

            if (team.getId() != null && team.getId().equals(alreadyExistingEntityId)) {
                throw new EntityExistsException("This is behaviour of EntityManager");
            }

            if (team.getName() == null
                    || team.getName().equals(alreadyExistingTeamName)
                    || team.getMembers() == null
                    || team.getTeamLeader() == null
                    || !team.getMembers().contains(team.getTeamLeader())) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            team.setId(createdEntityId);
            return null; //this is happy day scenario
        }).when(teamDao).create(any(Team.class));

        //update
        doAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            Team team = (Team) invocation.getArguments()[0];

            if (team.getName() == null
                    || team.getName().equals(alreadyExistingTeamName)
                    || team.getMembers() == null
                    || team.getTeamLeader() == null
                    || !team.getMembers().contains(team.getTeamLeader())) {
                throw new ConstraintViolationException("This is behaviour of validation on entities", null);
            }
            if (team.getId() == null) {
                team.setId(updatedEntityId);//safe
            }

            return team; //this is happy day scenario
        }).when(teamDao).update(any(Team.class));

        //remove
        doAnswer((InvocationOnMock invocation) -> {
            Object argument = invocation.getArguments()[0];
            if (invocation.getArguments()[0] == null) {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            Team team = (Team) invocation.getArguments()[0];

            if (team.getId() == alreadyExistingEntityId) //happy day scenario
            {
                return null;
            }

            if (team.getId() == notPersistedEntityId) //entity is not saved
            {
                throw new InvalidDataAccessApiUsageException("This behaviour is already tested on dao layer.");
            }

            return null;
        }).when(teamDao).remove(any(Team.class));
    }

    @Test
    public void createTeam() {
        teamService.createTeam(newTeam);
        verify(teamDao).create(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), newTeam);
        assertNotNull(newTeam);
        assertEquals((long) newTeam.getId(), createdEntityId);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createTeamNull() {
        teamService.createTeam(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamExistingID() {
        newTeam.setId(alreadyExistingEntityId);
        teamService.createTeam(newTeam);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamWithExistingName() {
        newTeam.setName(alreadyExistingTeamName);
        teamService.createTeam(newTeam);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamNullName() {
        teamService.createTeam(new Team());
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamWithoutLeader() {
        newTeam.setTeamLeader(null);
        teamService.createTeam(newTeam);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void createTeamLeaderNotInMembers() {
        newTeam.removeMember(leader);
        teamService.createTeam(newTeam);
    }

    @Test
    public void updateTeam() {
        assertNotNull(teamPersisted.getId());
        Team updated = teamService.updateTeam(teamPersisted);
        verify(teamDao).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
        assertEquals(updated.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(updated, teamPersisted);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateTeamNull() {
        teamService.updateTeam(null);
    }

    @Test
    public void updateTeamNonExisting() {
        assertNull(newTeam.getId());
        Team updated = teamService.updateTeam(newTeam);
        verify(teamDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), newTeam);
        assertEquals((long) updated.getId(), updatedEntityId);
        assertDeepEqualsWithoutId(updated, newTeam);
    }

    @Test
    public void updateTeamChangeName() {
        assertNotNull(teamPersisted.getId());
        teamPersisted.setName("Change Name");
        Team updated = teamService.updateTeam(teamPersisted);
        verify(teamDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
        assertEquals(updated.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(updated, teamPersisted);
    }

    @Test
    public void updateTeamAddMember() {
        assertNotNull(teamPersisted.getId());
        User addMember = new User(3L);
        addMember.setEmail("addMember@mail.com");
        addMember.setPasswordHash("heslo123");
        addMember.setFirstName("Petr");
        addMember.setHeight(184);
        addMember.setLastName("Přidaný");
        addMember.setRole(UserRole.REGULAR);
        addMember.setSex(Sex.MALE);
        addMember.setWeight(85);
        addMember.setDateOfBirth(LocalDate.ofYearDay(1991, 1));

        teamPersisted.addMember(addMember);
        Team updated = teamService.updateTeam(teamPersisted);

        verify(teamDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
        assertEquals(updated.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(updated, teamPersisted);
    }

    @Test
    public void updateTeamRemoveMember() {
        assertNotNull(teamPersisted.getId());
        teamPersisted.removeMember(member);
        Team updated = teamService.updateTeam(teamPersisted);

        verify(teamDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
        assertEquals(updated.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(updated, teamPersisted);
    }

    @Test
    public void updateTeamChangeLeader() {
        assertNotNull(teamPersisted.getId());
        teamPersisted.setTeamLeader(member);
        Team updated = teamService.updateTeam(teamPersisted);

        verify(teamDao, atLeast(1)).update(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
        assertEquals(updated.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(updated, teamPersisted);
    }

    @Test
    public void findAllNonEmptyResult() {

        List<Team> entityList = Arrays.asList(teamPersisted, teamPersisted2);
        when(teamDao.findAll()).thenReturn(entityList);

        List<Team> resultList = teamService.getAllTeams();

        assertEquals(resultList.size(), entityList.size());

        //just to check no modification of persisted data is done on this layer
        for (int i = 0; i < entityList.size(); i++) {
            Team entity = entityList.get(i);
            Team result = resultList.get(i);
            assertEquals(result.getId(), entity.getId());
            assertDeepEqualsWithoutId(result, entity);
        }
    }

    @Test
    public void findAllEmptyResult() {
        when(teamDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(teamService.getAllTeams().size(), 0);
    }

    @Test
    public void findTeamById() {
        Team found = teamService.findTeamById(1L);
        assertEquals(found.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(found, teamPersisted);
    }

    @Test
    public void findTeamNotExistingById() {
        assertNull(teamService.findTeamById(0L));
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void findTeamByIdNull() {
        teamService.findTeamById(null);
    }

    @Test
    public void findTeamByName() {
        Team result = teamService.findTeamByName("frajeri");
        assertEquals(result.getId(), teamPersisted.getId());
        assertDeepEqualsWithoutId(result, teamPersisted);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findTeamByNameNull() {
        teamService.findTeamByName(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findTeamByNameEmpty() {
        teamService.findTeamByName("");
    }

    @Test
    public void findTeamNotExistingByName() {
        assertNull(teamService.findTeamByName("non existing"));
    }

    @Test
    public void removeTeam() {
        teamPersisted.setId(alreadyExistingEntityId);
        teamService.removeTeam(teamPersisted);
        verify(teamDao, atLeast(1)).remove(argumentCaptor.capture());
        assertDeepEqualsWithoutId(argumentCaptor.getValue(), teamPersisted);
    }

    @Test(expectedExceptions = IllegalArgumentException.class) // ActivityTrackerDataAccessException
    public void removeNull() {
        teamService.removeTeam(null);
    }

    @Test(expectedExceptions = ActivityTrackerDataAccessException.class)
    public void removeNonExisting() {
        teamPersisted.setId(notPersistedEntityId);
        teamService.removeTeam(teamPersisted);
    }

    private void assertDeepEqualsWithoutId(Team team1, Team team2) {
        assertEquals(team1.getName(), team2.getName());
        assertEquals(team1.getTeamLeader(), team2.getTeamLeader());
        assertEquals(team1.getMembers().size(), team2.getMembers().size());
        assertTrue(team1.getMembers().containsAll(team2.getMembers()));
    }
}
