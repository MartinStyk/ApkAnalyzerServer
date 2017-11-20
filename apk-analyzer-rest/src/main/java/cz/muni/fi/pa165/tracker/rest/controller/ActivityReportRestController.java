package cz.muni.fi.pa165.tracker.rest.controller;

import cz.muni.fi.pa165.tracker.dto.ActivityReportDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.ActivityReportFacade;
import cz.muni.fi.pa165.tracker.rest.exception.RequestedResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Rest controller for activity report.
 *
 * @author Martin Styk
 * @version 1.12.2016
 */
@RestController
@RequestMapping(ApiUris.REPORT)
public class ActivityReportRestController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityReportRestController.class);

    @Inject
    private ActivityReportFacade activityReportFacade;

    /**
     * If no parameter is specified retrieves list of all activity reports,
     * If parameter sportId is specified retrieves list of activity reports for given sport
     * If parameter userId is specified retrieves list of activity reports for given user
     * If parameter sportId is specified in conjunction with userId method retrieves reports for gicen sport and user
     * <p>
     * <p>
     * curl -i -X GET http://localhost:8080/pa165/rest/reports
     * curl -i -X GET http://localhost:8080/pa165/rest/reports?sportId=1
     * curl -i -X GET http://localhost:8080/pa165/rest/reports?userId=2
     * curl -i -X GET http://localhost:8080/pa165/rest/reports?userId=2&sportId=2
     *
     * @param sportId id of sport
     * @param userId  id of user
     * @return list of all activity reports (corresponding to params)
     * @throws RequestedResourceNotFoundException if sport od user requested by parameter doesn't exist
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<ActivityReportDTO> getReports(@RequestParam(value = "sportId", required = false) Long sportId,
                                                    @RequestParam(value = "userId", required = false) Long userId) {

        if (sportId == null && userId == null) {
            return activityReportFacade.getAllActivityReports();
        }

        try {
            if (userId != null && sportId != null)
                return activityReportFacade.getActivityReportsByUserAndSport(userId, sportId);

            if (sportId != null)
                return activityReportFacade.getActivityReportsBySport(sportId);

            if (userId != null)
                return activityReportFacade.getActivityReportsByUser(userId);

        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in getReports()", e);
            throw new RequestedResourceNotFoundException(e);
        }
        return null;
    }

    /**
     * Get activity report according to id
     * <p>
     * curl -i -X GET http://localhost:8080/pa165/rest/reports/{id}
     *
     * @param id report identifier as path variable
     * @return DTO of requested report
     * @throws RequestedResourceNotFoundException if report does not exist in database
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ActivityReportDTO findReportById(@PathVariable("id") long id) {
        try {
            return activityReportFacade.getActivityReportById(id);
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in findReportById()", e);
            throw new RequestedResourceNotFoundException(e);
        }
    }

    /**
     * Delete activity report by id.
     * <p>
     * curl -i -X DELETE http://localhost:8080/pa165/rest/reports/{id}
     * <p>
     *
     * @param id od report to delete
     * @throws RequestedResourceNotFoundException if report is not found
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteReport(@PathVariable("id") long id) throws Exception {
        try {
            activityReportFacade.removeActivityReport(id);
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            logger.error("Exception in deleteReport()", e);
            throw new RequestedResourceNotFoundException(e);
        }
    }

}
