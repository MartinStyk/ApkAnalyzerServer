package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.dto.*;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.service.TeamService;
import cz.muni.fi.pa165.tracker.service.TimeService;
import cz.muni.fi.pa165.tracker.service.UserService;
import cz.muni.fi.pa165.tracker.service.UserStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class implemented user facade.
 *
 * @author Petra Ondřejková
 * @version 09.11. 2016
 */
@Service
@Transactional
public class UserFacadeImpl implements UserFacade {

    @Inject
    private UserService userService;

    @Inject
    private TeamService teamService;

    @Inject
    private UserStatisticsService statisticsService;

    @Inject
    private TimeService timeService;

    @Inject
    private BeanMappingService bms;

    @Override
    public Long createUser(UserCreateDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null. ");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("Password is null or empty. ");
        }

        userService.registerUser(bms.mapTo(user, User.class), user.getPasswordHash());
        return userService.findByEmail(user.getEmail()).getId();
    }

    @Override
    public void updateUser(UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        User userEntity = bms.mapTo(user, User.class);
        if (userService.findById(userEntity.getId()) == null) {
            throw new NonExistingEntityException("Cannot update nonexisting user.");
        }
        //we can also change team
        //since DTO contains only team name, we need to convert it to Team object here
        if (user.getTeam() != null) {
            Team team = teamService.findTeamByName(user.getTeam());
            if (team == null) {
                throw new NonExistingEntityException("Cannot set non existing team for user.");
            }
            userEntity.setTeam(team);
        }
        userService.update(userEntity);
    }

    @Override
    public UserDTO findUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("userId is null");
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new NonExistingEntityException("User doesn't exist for id " + id);
        }
        UserDTO userDTO = bms.mapTo(user, UserDTO.class);
        userDTO.setTotalCalories(statisticsService.getTotalCalories(user));
        if (user.getTeam() != null) {
            userDTO.setTeam(user.getTeam().getName());
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> resultEntities = userService.findAll();
        List<UserDTO> resultDtos = new ArrayList<>(resultEntities.size());
        for (User user : resultEntities) {
            UserDTO dto = bms.mapTo(user, UserDTO.class);
            dto.setTotalCalories(statisticsService.getTotalCalories(user));
            if (user.getTeam() != null) {
                dto.setTeam(user.getTeam().getName());
            }
            resultDtos.add(dto);
        }
        return resultDtos;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new NonExistingEntityException("User doesn't exist for email " + email);
        }
        UserDTO userDTO = bms.mapTo(user, UserDTO.class);
        userDTO.setTotalCalories(statisticsService.getTotalCalories(user));
        if (user.getTeam() != null) {
            userDTO.setTeam(user.getTeam().getName());
        }
        return userDTO;
    }

    @Override
    public void removeUser(UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        User userEntity = bms.mapTo(user, User.class);
        if (userService.findById(userEntity.getId()) == null) {
            throw new NonExistingEntityException("User does not exist");
        }
        userService.deleteUser(userEntity);
    }

    @Override
    public boolean logIn(UserAuthenticateDTO user) {
        User userEntity = userService.findById(user.getUserId());
        return userService.authenticateUser(userEntity, user.getPassword());
    }

    @Override
    public boolean isAdmin(UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        User userEntity = bms.mapTo(user, User.class);
        return userService.isAdmin(userEntity);
    }

    @Override
    public UserStatisticsDTO getStatistics(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        User user = userService.findById(userDTO.getId());
        if (user == null) {
            throw new NonExistingEntityException("User does not exist");
        }
        UserStatisticsDTO statisticsDTO = new UserStatisticsDTO();
        statisticsDTO.setUserDTO(bms.mapTo(user, UserDTO.class));

        statisticsDTO.setTotalCalories(statisticsService.getTotalCalories(user));
        statisticsDTO.setCaloriesLastWeek(
                statisticsService.getTotalCalories(user, timeService.dateWeekAgo(), timeService.dateNow())
        );
        statisticsDTO.setCaloriesLastMonth(
                statisticsService.getTotalCalories(user, timeService.dateMonthAgo(), timeService.dateNow())
        );

        statisticsDTO.setTotalSportActivities(statisticsService.getTotalActivities(user));
        statisticsDTO.setSportActivitiesLastWeek(
                statisticsService.getTotalActivities(user, timeService.dateWeekAgo(), timeService.dateNow())
        );
        statisticsDTO.setSportActivitiesLastMonth(
                statisticsService.getTotalActivities(user, timeService.dateMonthAgo(), timeService.dateNow())
        );

        Map<SportActivity, Integer> sportsAndCount = statisticsService.getSportsPerformedByUser(user);
        statisticsDTO.setSportActivities(
                bms.mapTo(sportsAndCount, SportActivityDTO.class)
        );

        Map<SportActivity, Integer> caloriesForSports = statisticsService.getCaloriesForSportsOfUser(user);
        statisticsDTO.setCaloriesForActivities(
                bms.mapTo(caloriesForSports, SportActivityDTO.class)
        );

        return statisticsDTO;
    }

    @Override
    public List<UserDTO> getUsersWithoutTeam() {
        return findAll().stream().filter(s -> s.getTeam() == null).collect(Collectors.toList());
    }
}
