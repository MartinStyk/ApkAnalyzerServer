/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.dto.TeamCreateDTO;
import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.service.TeamService;
import cz.muni.fi.pa165.tracker.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link TeamFacade}.
 *
 * @author Jan Grundmann
 * @version 20.11.2016
 */
@Service
@Transactional
public class TeamFacadeImpl implements TeamFacade {

    @Inject
    private TeamService teamService;

    @Inject
    private UserService userService;

    @Inject
    private BeanMappingService bms;

    @Override
    public Long createTeam(TeamCreateDTO teamDTO) {
        if (teamDTO == null) {
            throw new IllegalArgumentException("TeamDTO is null");
        }
        Team team = bms.mapTo(teamDTO, Team.class);
        User leader = getExistingUserOrThrowException(teamDTO.getTeamLeaderId());
        team.setTeamLeader(leader);
        team.addMember(leader);
        leader.setTeam(team);

        teamService.createTeam(team);
        userService.update(leader);
        return team.getId();
    }

    @Override
    public void updateTeam(TeamDTO teamDTO) {
        if (teamDTO == null) {
            throw new IllegalArgumentException("TeamDTO is null");
        }
        Team team = bms.mapTo(teamDTO, Team.class);
        List<User> members = bms.mapTo(teamDTO.getMembers(), User.class);
        members.forEach(team::addMember);

        if (teamService.findTeamById(team.getId()) == null) {
            throw new NonExistingEntityException("Cannot update nonexisting team.");
        }
        //set owning side
        for (User u : team.getMembers()) {
            u.setTeam(team);
            userService.update(u);
        }

        teamService.updateTeam(team);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return bms.mapTo(teamService.getAllTeams(), TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamById(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("teamId is null");
        }
        Team team = teamService.findTeamById(teamId);
        if (team == null) {
            throw new NonExistingEntityException("Team doesn't exist");
        }
        TeamDTO teamDTO = bms.mapTo(team, TeamDTO.class);
        List<UserDTO> members = bms.mapTo(team.getMembers(), UserDTO.class);
        for (UserDTO user : members) {
            user.setTeam(team.getName());
        }
        teamDTO.setMembers(members);
        teamDTO.getTeamLeader().setTeam(team.getName());

        return teamDTO;
    }

    @Override
    public TeamDTO getTeamByName(String teamName) {
        if (teamName == null) {
            throw new IllegalArgumentException("Team Name is null");
        }
        Team team = teamService.findTeamByName(teamName);
        if (team == null) {
            throw new NonExistingEntityException("Team doesn't exist");
        }
        TeamDTO teamDTO = bms.mapTo(team, TeamDTO.class);
        List<UserDTO> members = bms.mapTo(team.getMembers(), UserDTO.class);
        for (UserDTO user : members) {
            user.setTeam(team.getName());
        }
        teamDTO.setMembers(members);
        teamDTO.getTeamLeader().setTeam(team.getName());

        return teamDTO;
    }

    @Override
    public void removeTeam(TeamDTO teamDTO) {
        if (teamDTO == null) {
            throw new IllegalArgumentException("TeamDTO is null");
        }
        if (teamService.findTeamById(teamDTO.getId()) == null) {
            throw new NonExistingEntityException("Team doesn't exist");
        }
        Team team = bms.mapTo(teamDTO, Team.class);
        List<User> members = bms.mapTo(teamDTO.getMembers(), User.class);
        members.forEach(team::addMember);

        teamService.removeTeam(team);
    }

    @Override
    public void removeUserFromTeam(TeamDTO teamDTO, UserDTO userDTO) {
        if (teamDTO == null) {
            throw new IllegalArgumentException("TeamDTO is null");
        }
        if (teamService.findTeamById(teamDTO.getId()) == null) {
            throw new NonExistingEntityException("Team doesn't exist");
        }
        User user = getExistingUserOrThrowException(userDTO.getId());
        user.setTeam(null);

        userService.update(user);
    }

    @Override
    public void addUserToTeam(TeamDTO teamDTO, UserDTO userDTO) {
        if (teamDTO == null) {
            throw new IllegalArgumentException("TeamDTO is null");
        }
        Team team = teamService.findTeamById(teamDTO.getId());
        if (team == null) {
            throw new NonExistingEntityException("Team doesn't exist");
        }
        User user = getExistingUserOrThrowException(userDTO.getId());
        user.setTeam(team);

        userService.update(user);
    }

    /**
     * @param id user id
     * @return existing user
     * @throws NonExistingEntityException if user does not exist
     * @throws IllegalArgumentException   if id is null
     */
    private User getExistingUserOrThrowException(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("user id is null");
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new NonExistingEntityException("User does not exist. Id: " + id);
        }
        return user;
    }

}
