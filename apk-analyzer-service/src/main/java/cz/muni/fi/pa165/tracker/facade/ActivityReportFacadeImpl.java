package cz.muni.fi.pa165.tracker.facade;

import cz.muni.fi.pa165.tracker.dto.ActivityReportCreateDTO;
import cz.muni.fi.pa165.tracker.dto.ActivityReportDTO;
import cz.muni.fi.pa165.tracker.dto.ActivityReportUpdateDTO;
import cz.muni.fi.pa165.tracker.entity.ActivityReport;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.mapping.BeanMappingService;
import cz.muni.fi.pa165.tracker.service.ActivityReportService;
import cz.muni.fi.pa165.tracker.service.SportActivityService;
import cz.muni.fi.pa165.tracker.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ActivityReportFacade}.
 *
 * @author Martin Styk
 * @version 07.11.2016
 */
@Service
@Transactional
public class ActivityReportFacadeImpl implements ActivityReportFacade {

    @Inject
    private ActivityReportService activityReportService;

    @Inject
    private BeanMappingService beanMappingService;

    @Inject
    private UserService userService;

    @Inject
    private SportActivityService sportService;

    @Override
    public Long createActivityReport(ActivityReportCreateDTO activityReportDTO) {
        if (activityReportDTO == null) {
            throw new IllegalArgumentException("activity report cannot be null");
        }
        ActivityReport activityReport = beanMappingService.mapTo(activityReportDTO, ActivityReport.class);

        activityReport.setUser(getExistingUserOrThrowException(activityReportDTO.getUserId()));
        activityReport.setSportActivity(getExistingSportOrThrowException(activityReportDTO.getSportActivityId()));

        activityReportService.create(activityReport);
        return activityReport.getId();
    }

    @Override
    public void updateActivityReport(ActivityReportUpdateDTO activityReportDTO) {
        if (activityReportDTO == null) {
            throw new IllegalArgumentException("activity report cannot be null");
        }
        ActivityReport activityReport = beanMappingService.mapTo(activityReportDTO, ActivityReport.class);

        if (activityReportService.findById(activityReport.getId()) == null) {
            throw new NonExistingEntityException("Can not update non existing activity report");
        }

        activityReport.setUser(getExistingUserOrThrowException(activityReportDTO.getUserId()));
        activityReport.setSportActivity(getExistingSportOrThrowException(activityReportDTO.getSportActivityId()));

        activityReportService.update(activityReport);
    }

    @Override
    public List<ActivityReportDTO> getAllActivityReports() {
        return performUsersTeamConversion(activityReportService.findAll());
    }

    @Override
    public ActivityReportDTO getActivityReportById(Long activityReportId) {
        if (activityReportId == null) {
            throw new IllegalArgumentException("activityReportId is null");
        }
        ActivityReport activityReport = activityReportService.findById(activityReportId);
        if (activityReport == null) {
            throw new NonExistingEntityException("Activity report for doesn't exist");
        }
        return beanMappingService.mapTo(activityReport, ActivityReportDTO.class);
    }

    @Override
    public List<ActivityReportDTO> getActivityReportsByUser(Long userId) {
        User user = getExistingUserOrThrowException(userId);
        return performUsersTeamConversion(activityReportService.findByUser(user));
    }

    @Override
    public List<ActivityReportDTO> getActivityReportsBySport(Long sportId) {
        SportActivity sportActivity = getExistingSportOrThrowException(sportId);
        return performUsersTeamConversion(activityReportService.findBySport(sportActivity));
    }

    @Override
    public List<ActivityReportDTO> getActivityReportsByUserAndSport(Long userId, Long sportId) {
        User user = getExistingUserOrThrowException(userId);
        SportActivity activity = getExistingSportOrThrowException(sportId);

        return performUsersTeamConversion(activityReportService.findByUserAndSport(user, activity));
    }

    @Override
    public void removeActivityReport(Long activityReportId) {
        if (activityReportId == null) {
            throw new IllegalArgumentException("activityReportId is null");
        }
        ActivityReport activityReport = activityReportService.findById(activityReportId);
        if (activityReport == null) {
            throw new NonExistingEntityException("Can not remove not existing activity report");
        }
        activityReportService.remove(activityReport);
    }

    @Override
    public void removeActivityReportsOfUser(Long userId) {
        User user = getExistingUserOrThrowException(userId);
        activityReportService.removeActivityReportsOfUser(user);
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

    /**
     * @param id sport id
     * @return existing sport
     * @throws NonExistingEntityException if sport does not exist
     * @throws IllegalArgumentException   if id is null
     */
    private SportActivity getExistingSportOrThrowException(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("sport id is null");
        }
        SportActivity sportActivity = sportService.findById(id);
        if (sportActivity == null) {
            throw new NonExistingEntityException("Sport does not exist. Id: " + id);
        }
        return sportActivity;
    }

    /**
     * This is needed to translate team object into team name which is used in UserDTO
     *
     * @param activityReports entity list
     * @return dto list with correctly set user team
     */
    private List<ActivityReportDTO> performUsersTeamConversion(List<ActivityReport> activityReports) {
        List<ActivityReportDTO> activityReportDTOs = new ArrayList<>(activityReports.size());
        for (ActivityReport report : activityReports) {
            ActivityReportDTO dto = beanMappingService.mapTo(report, ActivityReportDTO.class);
            if ((report.getUser()) != null && report.getUser().getTeam() != null) {
                dto.getUser().setTeam(report.getUser().getTeam().getName());
            }
            activityReportDTOs.add(dto);
        }
        return activityReportDTOs;
    }
}
