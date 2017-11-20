package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.configuration.ServiceConfiguration;
import cz.muni.fi.pa165.tracker.dto.TeamCreateDTO;
import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.enums.Sex;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingServiceImpl;
import cz.muni.fi.pa165.tracker.service.TeamService;
import cz.muni.fi.pa165.tracker.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.*;
import static org.mockito.Mockito.atLeast;

/**
 * 
 * @author Petra Ondřejková
 * @version 18.11. 2016
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class TeamFacadeTest  extends AbstractTestNGSpringContextTests {
    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Spy
    @Inject
    private final BeanMappingService beanMappingService = new BeanMappingServiceImpl();

    @InjectMocks
    private final TeamFacade teamFacade = new TeamFacadeImpl();

    @Captor
    ArgumentCaptor<Team> argumentCaptor;

    private User leader;
    private User leader2;
    private UserDTO leaderDTO;
    private UserDTO leader2DTO;
    private Team team;
    private TeamDTO teamDTO;
    private Team team2;

    @BeforeClass
    public void setupMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initTeamsAndUsers() {
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

        leaderDTO = new UserDTO();
        leaderDTO.setId(1L);
        leaderDTO.setEmail("pepa@mail.com");
        leaderDTO.setPasswordHash(leader.getPasswordHash());
        leaderDTO.setFirstName(leader.getFirstName());
        leaderDTO.setHeight(leader.getHeight());
        leaderDTO.setLastName(leader.getLastName());
        leaderDTO.setRole(leader.getRole());
        leaderDTO.setSex(leader.getSex());
        leaderDTO.setWeight(leader.getWeight());
        leaderDTO.setDateOfBirth(leader.getDateOfBirth());
        leaderDTO.setTotalCalories(0);
        leaderDTO.setTeam("TEAM");

        leader2 = new User(2L);
        leader2.setEmail("josef@mail.com");
        leader2.setPasswordHash("heslo");
        leader2.setFirstName("Josef");
        leader2.setHeight(150);
        leader2.setLastName("Stary");
        leader2.setRole(UserRole.REGULAR);
        leader2.setSex(Sex.MALE);
        leader2.setWeight(80);
        leader2.setDateOfBirth(LocalDate.ofYearDay(1991, 333));

        leader2DTO = new UserDTO();
        leader2DTO.setId(2L);
        leader2DTO.setEmail("josef@mail.com");
        leader2DTO.setPasswordHash(leader2.getPasswordHash());
        leader2DTO.setFirstName(leader2.getFirstName());
        leader2DTO.setHeight(leader2.getHeight());
        leader2DTO.setLastName(leader2.getLastName());
        leader2DTO.setRole(leader2.getRole());
        leader2DTO.setSex(leader2.getSex());
        leader2DTO.setWeight(leader2.getWeight());
        leader2DTO.setDateOfBirth(leader2.getDateOfBirth());
        leader2DTO.setTotalCalories(0);
        leader2DTO.setTeam("TEAM 2");

        team = new Team();
        team.setName("TEAM");
        team.setTeamLeader(leader);
        team.addMember(leader);

        teamDTO = new TeamDTO();
        teamDTO.setName("TEAM");
        teamDTO.setTeamLeader(leaderDTO);
        List<UserDTO> members = new ArrayList<>();
        members.add(leaderDTO);
        teamDTO.setMembers(members);

        team2 = new Team();
        team2.setName("TEAM 2");
        team2.setTeamLeader(leader2);
        team2.addMember(leader2);
    }

    @BeforeMethod(dependsOnMethods = "initTeamsAndUsers")
    public void initMocksBehaviour() {
        // findById
        when(userService.findById(1L)).thenReturn(leader);
        when(teamService.findTeamById(0L)).thenReturn(null);
        when(teamService.findTeamById(1L)).thenReturn(team);

        //findByName
        when(teamService.findTeamByName("TEAM")).thenReturn(team);
        when(teamService.findTeamByName("non existing")).thenReturn(null);
    }

    @Test
    public void testClassInitializationTest() {
        assertNotNull(teamService);
        assertNotNull(beanMappingService);
        assertNotNull(teamFacade);
        assertNotNull(userService);
    }

    @Test
    public void createTeamTest() {
        final String name = "New Team";
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO();
        teamCreateDTO.setName(name);
        teamCreateDTO.setTeamLeaderId(1L);

        teamFacade.createTeam(teamCreateDTO);
        verify(teamService).createTeam(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getName(), name);
        assertDeepEquals(argumentCaptor.getValue().getTeamLeader(), leader);
        assertEquals(argumentCaptor.getValue().getMembers().size(), 1);
        assertDeepEquals(argumentCaptor.getValue().getMembers().get(0), leader);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createTeamTestWithNull() {
        teamFacade.createTeam(null);
    }

    @Test
    public void updateTeamTestName() {
        final long id = 1;
        final String name = "Updated Team";
        TeamDTO updatedDTO = new TeamDTO();
        updatedDTO.setId(id);
        // update name
        updatedDTO.setName(name);
        updatedDTO.setTeamLeader(leaderDTO);
        List<UserDTO> members = new ArrayList<>();
        members.add(leaderDTO);
        updatedDTO.setMembers(members);

        teamFacade.updateTeam(updatedDTO);
        verify(teamService, atLeast(1)).updateTeam(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), id);
        assertEquals(argumentCaptor.getValue().getName(), name);
        assertEquals(argumentCaptor.getValue().getTeamLeader(), leader);
        assertEquals(argumentCaptor.getValue().getMembers().size(), members.size());
    }

    @Test
    public void updateTeamTestLeader() {
        final long id = 1;
        TeamDTO updatedDTO = new TeamDTO();
        updatedDTO.setId(id);
        updatedDTO.setName(team.getName());
        updatedDTO.setTeamLeader(leader2DTO);
        List<UserDTO> members = new ArrayList<>();
        members.add(leader2DTO);
        members.add(leaderDTO);
        updatedDTO.setMembers(members);

        teamFacade.updateTeam(updatedDTO);
        verify(teamService).updateTeam(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), id);
        assertEquals(argumentCaptor.getValue().getName(), team.getName());
        assertEquals(argumentCaptor.getValue().getTeamLeader(), leader2);
        assertEquals(argumentCaptor.getValue().getMembers().size(), members.size());
    }

    @Test
    public void updateTeamTestMembers() {
        final long id = 1;
        TeamDTO updatedDTO = new TeamDTO();
        updatedDTO.setId(id);
        updatedDTO.setName(team.getName());
        updatedDTO.setTeamLeader(leaderDTO);
        List<UserDTO> members = new ArrayList<>();
        members.add(leader2DTO);
        members.add(leaderDTO);
        updatedDTO.setMembers(members);

        teamFacade.updateTeam(updatedDTO);
        verify(teamService, atLeast(1)).updateTeam(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), id);
        assertEquals(argumentCaptor.getValue().getName(), team.getName());
        assertEquals(argumentCaptor.getValue().getTeamLeader(), leader);
        assertEquals(argumentCaptor.getValue().getMembers().size(), members.size());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void updateNonExistingTeamTest() {
        final long id = 0; //mock configured to return null as when sport is not found in db
        final String name = "Updated Sport";
        TeamDTO updatedDTO = new TeamDTO();
        updatedDTO.setId(id);
        updatedDTO.setName(name);
        List<UserDTO> members = new ArrayList<>();
        members.add(leaderDTO);
        updatedDTO.setMembers(members);

        teamFacade.updateTeam(updatedDTO);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateTeamNullTest() {
        teamFacade.updateTeam(null);
    }

    @Test
    public void removeTeamTest() {
        //removing team with id 1 -  mock is initialized to return team
        TeamDTO removeDTO = new TeamDTO();
        removeDTO.setId(1L);
        removeDTO.setName("TEAM");
        List<UserDTO> members = new ArrayList<>();
        members.add(leaderDTO);
        removeDTO.setMembers(members);

        teamFacade.removeTeam(removeDTO);
        verify(teamService).removeTeam(argumentCaptor.capture());
        assertEquals((long) argumentCaptor.getValue().getId(), 1L);
        assertEquals(argumentCaptor.getValue().getName(), team.getName());
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void removeTeamNonExistingTest() {
        //removing team with id 0 -  mock is initialized to return null - not found
        TeamDTO team0 = new TeamDTO();
        team0.setId(0L);
        teamFacade.removeTeam(team0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void removeNullTest() {
        teamFacade.removeTeam(null);
    }

    @Test
    public void getTeamByIdTest() {
        TeamDTO team1 = teamFacade.getTeamById(1L);
        assertEquals(team1.getId(), teamDTO.getId());
        assertEquals(team1.getName(), teamDTO.getName());
        assertEquals(team1.getTeamLeader().getId(), teamDTO.getTeamLeader().getId());
        assertEquals(team1.getMembers().size(), teamDTO.getMembers().size());
        assertDeepEqualsDTO(team1.getMembers().get(0), teamDTO.getMembers().get(0));
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getNonExistingTeamByIdTest() {
        teamFacade.getTeamById(0L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullTeamByIdTest() {
        teamFacade.getTeamById(null);
    }

    @Test
    public void getByNameTest() {
        TeamDTO result = teamFacade.getTeamByName("TEAM");
        assertEquals(result.getId(), teamDTO.getId());
        assertEquals(result.getName(), teamDTO.getName());
        assertEquals(result.getTeamLeader().getId(), teamDTO.getTeamLeader().getId());
        assertDeepEqualsDTO(result.getTeamLeader(), teamDTO.getTeamLeader());
        assertEquals(result.getMembers().size(), teamDTO.getMembers().size());
        for(int i = 0; i < teamDTO.getMembers().size(); i++) {
            assertDeepEqualsDTO(result.getMembers().get(i), teamDTO.getMembers().get(i));
        }
    }

    @Test(expectedExceptions = NonExistingEntityException.class)
    public void getNonExistingTeamByNameTest() {
        teamFacade.getTeamByName("non existing");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getNullTeamByNameTest() {
        teamFacade.getTeamByName(null);
    }

    @Test
    public void getAllTeamsTest() {
        List<Team> entityList = Arrays.asList(team, team2);
        when(teamService.getAllTeams()).thenReturn(entityList);

        List<TeamDTO> dtoList = teamFacade.getAllTeams();

        assertEquals(dtoList.size(), 2);

        for (int i = 0; i < 2; i++) {
            Team entity = entityList.get(i);
            TeamDTO dto = dtoList.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getName(), entity.getName());
            assertEquals(dto.getTeamLeader().getEmail(), entity.getTeamLeader().getEmail());
            assertEquals(dto.getMembers().get(0).getEmail(), entity.getMembers().get(0).getEmail());
        }
    }

    @Test
    public void getAllTeamsEmptyTest() {
        when(teamService.getAllTeams()).thenReturn(new ArrayList<>());

        List<TeamDTO> dtoList = teamFacade.getAllTeams();
        assertNotNull(dtoList);
        assertEquals(dtoList.size(), 0);
    }

    @Test
    public void removeUserFromTeam() {
        when(userService.findById(2l)).thenReturn(new User());
        TeamDTO removeDTO = new TeamDTO();
        removeDTO.setId(1L);
        removeDTO.setName("TEAM");
        List<UserDTO> members = Arrays.asList(leaderDTO, new UserDTO(2));
        removeDTO.setMembers(members);

        teamFacade.removeUserFromTeam(removeDTO, new UserDTO(2));
        verify(userService, atLeast(1)).update(new User());
    }

    @Test
    public void addUserToTeam() {
        when(userService.findById(2l)).thenReturn(new User());
        TeamDTO removeDTO = new TeamDTO();
        removeDTO.setId(1L);
        removeDTO.setName("TEAM");
        List<UserDTO> members = Arrays.asList(leaderDTO);
        removeDTO.setMembers(members);

        teamFacade.addUserToTeam(removeDTO, new UserDTO(2));
        verify(userService, atLeast(1)).update(new User());
    }

    private void assertDeepEquals(User user1, User user2) {
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
    private void assertDeepEqualsDTO(UserDTO user1, UserDTO user2) {
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