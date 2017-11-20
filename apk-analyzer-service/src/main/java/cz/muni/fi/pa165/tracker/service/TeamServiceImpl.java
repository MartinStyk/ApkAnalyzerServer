/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.exception.TranslatePersistenceExceptions;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link TeamService}.
 *
 * @author Jan Grundmann
 * @version 20.11.2016
 */
@Service
@TranslatePersistenceExceptions
public class TeamServiceImpl implements TeamService {

    @Inject
    private TeamDao teamDao;

    @Inject
    private UserService userService;

    @Override
    public void createTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team entity is null");
        }
        teamDao.create(team);
    }

    @Override
    public Team updateTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team entity is null");
        }
        return teamDao.update(team);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamDao.findAll();
    }

    @Override
    public Team findTeamById(Long id) {
        return teamDao.findById(id);
    }

    @Override
    public Team findTeamByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name of team is null or empty");
        }
        return teamDao.findByName(name);
    }

    @Override
    public void removeTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team is null");
        }
        //remove users from team
        for (User user : team.getMembers()) {
            user.setTeam(null);
            userService.update(user);
        }

        teamDao.remove(team);
    }
}
