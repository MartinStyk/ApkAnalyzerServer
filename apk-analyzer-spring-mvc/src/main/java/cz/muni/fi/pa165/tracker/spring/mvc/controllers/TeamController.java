/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.spring.mvc.controllers;

import cz.muni.fi.pa165.tracker.dto.TeamCreateDTO;
import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.TeamUpdateDTO;
import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.TeamFacade;
import cz.muni.fi.pa165.tracker.facade.UserFacade;
import cz.muni.fi.pa165.tracker.spring.mvc.validator.UniqueTeamNameCreateValidator;
import cz.muni.fi.pa165.tracker.spring.mvc.validator.UniqueTeamNameUpdateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Grundmann
 * @version 6.12.2016
 */
@Controller
@RequestMapping(value = {"/teams"})
public class TeamController extends ActivityTrackerController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    @Inject
    private TeamFacade teamFacade;

    @Inject
    private UserFacade userFacade;

    @Inject
    private UniqueTeamNameCreateValidator uniqueTeamNameCreateValidator;

    @Inject
    private UniqueTeamNameUpdateValidator uniqueTeamNameUpdateValidator;

    /**
     * Shows all teams..
     *
     * @param model
     * @param teamName if specified, team with given name is returned
     * @return jsp template team/index
     */
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(value = "teamName", required = false) String teamName,
                        RedirectAttributes redirectAttributes,
                        UriComponentsBuilder uriComponentsBuilder) {

        teamName = getUtf8RequestParam(teamName);

        List<TeamDTO> teams = null;
        if (teamName == null || teamName.isEmpty()) {
            teams = teamFacade.getAllTeams();
        } else {
            teams = new ArrayList<>();
            try {
                teams.add(teamFacade.getTeamByName(teamName));
            } catch (NonExistingEntityException | IllegalArgumentException e) {
                log.debug("Team " + teamName + " not found", e);

                redirectAttributes.addFlashAttribute("alert_warning", "Team " + teamName + " not found.");
                return "redirect:" + uriComponentsBuilder.path("/teams").build().encode().toUriString();
            }
        }
        model.addAttribute("teams", teams);
        model.addAttribute("isUserInTeam", userFacade.getUsersWithoutTeam().contains(getLoggedUser()));

        return "teams/index";
    }

    /**
     * Team detail page
     *
     * @return jsp template team/create
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id, Model model) {

        TeamDTO team = teamFacade.getTeamById(id);
        log.debug("team detail({})", team);

        model.addAttribute("team", team);
        return "teams/detail";
    }

    /**
     * Initialize form for creation of a new team
     *
     * @param model
     * @return jsp template team/create
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("teamCreate", new TeamCreateDTO());
        return "teams/create";
    }

    /**
     * Handles the creation of new team
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute("teamCreate") TeamCreateDTO formData,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriComponentsBuilder) {

        log.debug("create team({})", formData);

        if (bindingResult.hasErrors()) {
            addValidationErrors(bindingResult, model);
            return "teams/create";
        }

        Long id = teamFacade.createTeam(formData);

        redirectAttributes.addFlashAttribute("alert_success", "Team with id " + id + " created");

        return "redirect:" + uriComponentsBuilder.path("/teams").buildAndExpand(id).encode().toUriString();
    }

    /**
     * Initialize form for update of team with given id
     *
     * @param model
     * @return jsp template team/update
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(id)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Permission denied.");
            return "redirect:/teams/detail/" + id;
        }


        TeamDTO found = teamFacade.getTeamById(id);

        TeamUpdateDTO updateDTO = new TeamUpdateDTO();
        updateDTO.setId(found.getId());
        updateDTO.setName(found.getName());
        updateDTO.setTeamLeader(found.getTeamLeader());
        updateDTO.setTeamLeaderId(updateDTO.getTeamLeader().getId());
        updateDTO.setMembers(found.getMembers());

        model.addAttribute("teamUpdate", updateDTO);

        return "teams/update";
    }

    /**
     * Handles the update of team
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(
            @Valid @ModelAttribute("teamUpdate") TeamUpdateDTO formData,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriBuilder) {

        if (!checkEditPermissions(id)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/detail/" + id;
        }
        log.debug("update team({})", formData);

        formData.setTeamLeader(userFacade.findUserById(formData.getTeamLeaderId()));
        formData.setMembers(teamFacade.getTeamById(id).getMembers());
        if (bindingResult.hasErrors()) {
            addValidationErrors(bindingResult, model);
            return "/teams/update";
        }

        teamFacade.updateTeam(formData);
        redirectAttributes.addFlashAttribute("alert_success", "Team " + formData.getName() + " updated");

        return "redirect:" + uriBuilder.path("/teams/detail/" + id).build().encode().toUriString();
    }

    /**
     * Removes team with given id
     *
     * @param model
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String remove(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder,
                         RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(id)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/";
        }

        log.debug("remove team({})", id);

        try {
            teamFacade.removeTeam(teamFacade.getTeamById(id));
            redirectAttributes.addFlashAttribute("alert_success", "Team with id " + id + " deleted");
        } catch (DataAccessException e) {
            log.error("Could not delete " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("alert_danger", "Team with id " + id + " can not be deleted");
        }
        return "redirect:" + uriBuilder.path("/teams").toUriString();
    }

    /**
     * Removes user from team with given id
     *
     * @param model
     */
    @RequestMapping(value = "/removeUsers/{teamId}", method = RequestMethod.GET)
    public String removeUser(@PathVariable long teamId, Model model, RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(teamId)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/detail/" + teamId;
        }

        TeamDTO currentTeam = teamFacade.getTeamById(teamId);

        //prepare all team members except leader - we can not remove him
        List<UserDTO> members = currentTeam.getMembers();
        members.remove(currentTeam.getTeamLeader());

        TeamDTO tempTeam = new TeamDTO();
        tempTeam.setMembers(members);

        model.addAttribute("users", tempTeam);
        model.addAttribute("currentTeam", currentTeam);
        return "/teams/removeUsers";
    }

    @RequestMapping(value = "/removeUsers/{teamId}", method = RequestMethod.POST)
    public String removeUser(@PathVariable long teamId, UriComponentsBuilder uriBuilder, @RequestBody String body,
                             RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(teamId)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/detail/" + teamId;
        }

        String[] idS = body.split("[&]*members=|[&]*[_]+members=[\\d]*");

        TeamDTO team = teamFacade.getTeamById(teamId);
        for (String id : idS) {
            if (!id.isEmpty())
                teamFacade.removeUserFromTeam(team, new UserDTO(Long.parseLong(id)));
        }

        return "redirect:" + uriBuilder.path("/teams/detail/" + teamId).toUriString();
    }

    /**
     * Add users to team
     *
     * @param model
     */
    @RequestMapping(value = "/addUsers/{teamId}", method = RequestMethod.GET)
    public String addUsers(@PathVariable long teamId, Model model, RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(teamId)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/detail/" + teamId;
        }

        TeamDTO currentTeam = teamFacade.getTeamById(teamId);
        List<UserDTO> withoutTeam = userFacade.getUsersWithoutTeam();
        TeamDTO tempTeam = new TeamDTO();
        tempTeam.setMembers(withoutTeam);
        model.addAttribute("users", tempTeam);
        model.addAttribute("currentTeam", currentTeam);
        return ("/teams/addUsers");
    }

    /**
     * Add users to team
     */
    @RequestMapping(value = "/addUsers/{teamId}", method = RequestMethod.POST)
    public String addUsers(@PathVariable long teamId, UriComponentsBuilder uriBuilder, @RequestBody String body,
                           RedirectAttributes redirectAttributes) {
        if (!checkEditPermissions(teamId)) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/teams/detail/" + teamId;
        }

        String[] idS = body.split("[&]*members=|[&]*[_]+members=[\\d]*");
        TeamDTO team = teamFacade.getTeamById(teamId);
        for (String id : idS) {
            if (!id.isEmpty())
                teamFacade.addUserToTeam(team, new UserDTO(Long.parseLong(id)));
        }

        return "redirect:" + uriBuilder.path("/teams/detail/" + teamId).toUriString();
    }

    @InitBinder
    protected void initUniqueConstrainBinder(WebDataBinder binder) {

        if (binder.getTarget() instanceof TeamCreateDTO
                && !(binder.getTarget() instanceof TeamDTO)) {
            binder.addValidators(uniqueTeamNameCreateValidator);
        }

        if (binder.getTarget() instanceof TeamUpdateDTO) {
            binder.addValidators(uniqueTeamNameUpdateValidator);
        }
    }

    private boolean checkEditPermissions(long teamId) {
        return checkEditPermissions(teamFacade.getTeamById(teamId));
    }

    private boolean checkEditPermissions(TeamDTO team) {
        return team.getTeamLeader().equals(getLoggedUser()) || getLoggedUser().getRole().equals(UserRole.ADMIN);
    }

}
