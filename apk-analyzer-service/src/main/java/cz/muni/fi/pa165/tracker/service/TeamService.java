package cz.muni.fi.pa165.tracker.service;

import java.util.List;

/**
 * Business logic for team
 *
 * @author Jan Grundmann
 * @version 20.11.2016
 */
public interface TeamService {

    /**
     * Creates a team
     * @param team team to be created
     */
    void createTeam(Team team);

    /**
     * Updates team
     * @param team updated team
     * @return update team
     */
    Team updateTeam(Team team);

    /**
     * Returns a list of all teams
     * @return list of all teams
     */
    List<Team> getAllTeams();

    /**
     * Returns team with given id
     * @param id id of desired team
     * @return found team
     */
    Team findTeamById(Long id);

    /**
     * Returns team with given name
     * @param name name of desired team
     * @return found team
     */
    Team findTeamByName(String name);

    /**
     * Removes given team
     * @param team team to be removed
     */
    void removeTeam(Team team);
}
