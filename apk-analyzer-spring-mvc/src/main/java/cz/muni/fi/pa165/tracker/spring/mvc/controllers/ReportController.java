package cz.muni.fi.pa165.tracker.spring.mvc.controllers;

import cz.muni.fi.pa165.tracker.dto.*;
import cz.muni.fi.pa165.tracker.facade.ActivityReportFacade;
import cz.muni.fi.pa165.tracker.facade.SportActivityFacade;
import cz.muni.fi.pa165.tracker.spring.mvc.validator.TimeSequenceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Adam Laurencik
 * @version 4.12.2016
 */
@Controller
@RequestMapping(value = {"/reports"})
public class ReportController extends ActivityTrackerController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Inject
    private ActivityReportFacade activityReportFacade;

    @Inject
    private SportActivityFacade sportActivityFacade;

    @Inject
    private MessageSource messageSource;

    /**
     * Default view for reports section
     *
     * @param model
     * @return jsp template reports/index
     */
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        UserDTO loggedInUser = getLoggedUser();
        List<ActivityReportDTO> reports = activityReportFacade.getActivityReportsByUser(loggedInUser.getId());
        model.addAttribute("reports", reports);
        return "reports/index";
    }

    /**
     * Initialize form for creation of new report
     *
     * @param model
     * @return jsp template reports/create
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        List<SportActivityDTO> sportActivities = sportActivityFacade.getAllSportActivities();
        model.addAttribute("sportActivities", sportActivities);
        model.addAttribute("reportCreate", new ActivityReportCreateDTO());
        return "reports/create";
    }

    /**
     * Handles the creation of new report
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @Valid @ModelAttribute("reportCreate") ActivityReportCreateDTO formData,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriComponentsBuilder) {

        log.debug("create report({})", formData);
        List<SportActivityDTO> sportActivities = sportActivityFacade.getAllSportActivities();
        model.addAttribute("sportActivities", sportActivities);
        //if there are any validation errors forward back to the the form
        if (bindingResult.hasErrors()) {
            addValidationErrors(bindingResult, model);
            return "/reports/create";
        }
        Long id = null;

        if (formData.getUserId() != getLoggedUser().getId()) {
            redirectAttributes.addFlashAttribute("alert_danger", "Create permission denied.");
            return "redirect:/";
        }
        id = activityReportFacade.createActivityReport(formData);
        redirectAttributes.addFlashAttribute("alert_success", "Activity report with id " + id + " created");
        return "redirect:" + uriComponentsBuilder.path("/reports").buildAndExpand(id).encode().toUriString();
    }

    /**
     * Initialize form for update of new report with given id
     *
     * @param model
     * @return jsp template reports/update
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        ActivityReportDTO found = activityReportFacade.getActivityReportById(id);
        if (found.getUser().getId() != getLoggedUser().getId()) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/";
        }
        List<SportActivityDTO> sportActivities = sportActivityFacade.getAllSportActivities();
        ActivityReportUpdateDTO updateDTO = new ActivityReportUpdateDTO();
        updateDTO.setId(found.getId());
        updateDTO.setEndTime(found.getEndTime());
        updateDTO.setStartTime(found.getStartTime());
        updateDTO.setSportActivityId(found.getSportActivity().getId());

        model.addAttribute("sportActivities", sportActivities);
        model.addAttribute("reportUpdate", updateDTO);

        return "reports/update";
    }

    /**
     * Handles the update of report
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(
            @Valid @ModelAttribute("reportUpdate") ActivityReportUpdateDTO formData,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriBuilder) {

        log.debug("update activityReport({})", formData);
        List<SportActivityDTO> sportActivities = sportActivityFacade.getAllSportActivities();
        model.addAttribute("sportActivities", sportActivities);
        if (bindingResult.hasErrors()) {
            addValidationErrors(bindingResult, model);
            return "/reports/update";
        }
        if (formData.getUserId() != getLoggedUser().getId()) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/";
        }
        activityReportFacade.updateActivityReport(formData);
        redirectAttributes.addFlashAttribute("alert_success", "Report " + formData.getId() + " updated");
        return "redirect:" + uriBuilder.path("/reports").build().encode().toUriString();
    }

    /**
     * Removes report with given id
     *
     * @param model
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String remove(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder,
            RedirectAttributes redirectAttributes) {
        ActivityReportDTO toBeRemoved = activityReportFacade.getActivityReportById(id);
        if (toBeRemoved.getUser().getId() != getLoggedUser().getId()) {
            redirectAttributes.addFlashAttribute("alert_danger", "Update permission denied.");
            return "redirect:/";
        }
        activityReportFacade.removeActivityReport(id);
        redirectAttributes.addFlashAttribute("alert_success", "Report deleted");
        return "redirect:" + uriBuilder.path("/reports").toUriString();
    }

    protected void addValidationErrors(BindingResult bindingResult, Model model) {
        for (FieldError fe : bindingResult.getFieldErrors()) {
            model.addAttribute(fe.getField() + "_error", true);
            log.error("FieldError: {}", fe);
        }
    }

    @InitBinder
    protected void initTimeSequenceBinder(WebDataBinder binder) {

        if (binder.getTarget() instanceof ActivityReportCreateDTO) {
            binder.addValidators(new TimeSequenceValidator());
        }
    }

}
