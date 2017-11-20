package cz.muni.fi.pa165.tracker.rest.controller;

import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.UserCreateDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.exception.ActivityTrackerDataAccessException;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.TeamFacade;
import cz.muni.fi.pa165.tracker.facade.UserFacade;
import cz.muni.fi.pa165.tracker.rest.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest controller for user entity.
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@RestController
@RequestMapping(ApiUris.USER)
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityReportRestController.class);

    @Inject
    private UserFacade userFacade;

    @Inject
    private TeamFacade teamFacade;

    /**
     * Get list of all users, or get list of users coresponding to given email
     * address (in case email parameter is specified).
     * <p>
     * curl -i -X GET http://localhost:8080/pa165/rest/users curl -i -X GET
     * http://localhost:8080/pa165/rest/users?email={value}
     *
     * @return list of all users registered in system, or list of users
     * corresponding to given email
     * @throws RequestedResourceNotFoundException if user requested by email is
     * not found
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<UserDTO> findUsers(@RequestParam(value = "email", required = false) String email) {
        if (email == null) {
            return userFacade.findAll();
        } else {
            try {
                List<UserDTO> result = new ArrayList<>();
                result.add(userFacade.findUserByEmail(email));
                return result;
            } catch (NonExistingEntityException | IllegalArgumentException e) {
                logger.error("Exception in findUsers()", e);
                throw new RequestedResourceNotFoundException(e);
            }
        }
    }

    /**
     * Get user according to id
     * <p>
     * curl -i -X GET http://localhost:8080/pa165/rest/users/{id}
     *
     * @param id user identifier as path variable
     * @return DTO of requested user
     * @throws RequestedResourceNotFoundException if user does not exist in
     * database
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final UserDTO findUserById(@PathVariable("id") long id) {
        try {
            return userFacade.findUserById(id);
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in findUserById()", e);
            throw new RequestedResourceNotFoundException(e);
        }
    }

    /**
     * Get team of user according to userid
     * <p>
     * curl -i -X GET http://localhost:8080/pa165/rest/users/{id}/team
     *
     * @param id user identifier as path variable
     * @return DTO of requested user
     * @throws RequestedResourceNotFoundException if user does not exist in
     * database
     */
    @RequestMapping(value = "/{id}/team", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final TeamDTO findUsersTeam(@PathVariable("id") long id) {
        try {
            UserDTO userDTO = userFacade.findUserById(id);
            return teamFacade.getTeamByName(userDTO.getTeam());
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in findUsersTeam()", e);
            throw new RequestedResourceNotFoundException(e);
        }
    }

    /**
     * Delete user by id.
     * <p>
     * curl -i -X DELETE http://localhost:8080/pa165/rest/users/{id}
     * <p>
     *
     * @param id od user to delete
     * @throws RequestedResourceNotFoundException if user is not found
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteUser(@PathVariable("id") long id) throws Exception {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userFacade.removeUser(userDTO);
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in deleteUser()", e);
            throw new RequestedResourceNotFoundException(e);
        } catch (DataAccessException e) {
            logger.error("Constrain violation in deleteUser()", e);
            throw new ResourceCanNotBeDeleted(e);
        }
    }

    /**
     * Create a new user by POST method.
     * <p>
     * curl -X POST -i -H "Content-Type: application/json" --data
     * '{"email":"mstyk@redhat.com","firstName":"Martin",
     * "lastName":"Styk","passwordHash":"200aaa","dateOfBirth":"2008-02-15","role":"ADMIN","sex":"MALE","height":"111",
     * "weight":"100"}' http://localhost:8080/pa165/rest/users
     *
     * @param user UserCreateDTO with required fields for creation
     * @return the newly created DTO of user
     * @throws InvalidResourceException if user can not be created because
     * validation failures
     * @throws ExistingResourceException if user already exists
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final UserDTO createUser(@RequestBody UserCreateDTO user) throws Exception {
        try {
            Long id = userFacade.createUser(user);
            return userFacade.findUserById(id);
        } catch (ActivityTrackerDataAccessException e) {
            logger.error("ActivityTrackerDataAccessException in createUser()", e);
            throw new InvalidResourceException(e);
        } catch (Exception e) {
            logger.error("Exception in createUser()", e);
            throw new ExistingResourceException(e);
        }
    }

    /**
     * Updates user by PUT method. Updates only fields specified in editedUser
     * <p>
     * curl -i -X PUT -H "Content-Type: application/json" --data
     * '{"email":"peterSagan@gmail.com","firstName":"Peter",
     * "lastName":"Sagan","passwordHash":"200aaa","dateOfBirth":"1990-01-28","role":"ADMIN","sex":"MALE","height":"111",
     * "weight":"100","team":"Rychle holky"}'
     * http://localhost:8080/pa165/rest/users/4
     * <p>
     * curl -i -X PUT -H "Content-Type: application/json" --data
     * '{"email":"peterSagan@gmail.com","firstName":"Petko"}'
     * http://localhost:8080/pa165/rest/users/4
     *
     * @param id user id as a path variable
     * @param editedUser UserDTO with required fields for update
     * @throws RequestedResourceNotFoundException if user does not exist
     * @throws ResourceNotModifiedException if user can not be updated
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final UserDTO updateUser(@PathVariable("id") long id, @RequestBody UserDTO editedUser) throws Exception {
        UserDTO existingUser = null;
        try {
            existingUser = userFacade.findUserById(id);
        } catch (Exception e) {
            logger.error("Exception in updateUser()", e);
            throw new RequestedResourceNotFoundException(e);
        }

        UserDTO toUpdateDTO = mergeDTOs(existingUser, editedUser);
        try {
            userFacade.updateUser(toUpdateDTO);
        } catch (Exception e) {
            logger.error("Exception in updateUser()", e);
            throw new ResourceNotModifiedException(e);
        }

        return toUpdateDTO;
    }

    /**
     * Merge existing DTO and DTO containing data to update
     */
    private UserDTO mergeDTOs(UserDTO existing, UserDTO toUpdate) {
        UserDTO result = new UserDTO();
        result.setId(existing.getId());
        result.setTotalCalories(existing.getTotalCalories());
        result.setPasswordHash((toUpdate.getPasswordHash() == null) ? existing.getPasswordHash()
                : toUpdate.getPasswordHash());
        result.setEmail((toUpdate.getEmail() == null) ? existing.getEmail() : toUpdate.getEmail());
        result.setFirstName((toUpdate.getFirstName() == null) ? existing.getFirstName() : toUpdate.getFirstName());
        result.setLastName((toUpdate.getLastName() == null) ? existing.getLastName() : toUpdate.getLastName());
        result.setDateOfBirth((toUpdate.getDateOfBirth() == null) ? existing.getDateOfBirth()
                : toUpdate.getDateOfBirth());
        result.setRole((toUpdate.getRole() == null) ? existing.getRole() : toUpdate.getRole());
        result.setSex((toUpdate.getSex() == null) ? existing.getSex() : toUpdate.getSex());
        result.setHeight((toUpdate.getHeight() < 1) ? existing.getHeight() : toUpdate.getHeight());
        result.setWeight((toUpdate.getWeight() < 1) ? existing.getWeight() : toUpdate.getWeight());
        result.setTeam((toUpdate.getTeam() == null) ? existing.getTeam() : toUpdate.getTeam());
        return result;
    }

}
