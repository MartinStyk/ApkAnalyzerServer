package sk.styk.martin.apkanalyzer.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.styk.martin.apkanalyzer.dto.AppDataDto;
import sk.styk.martin.apkanalyzer.dto.UsageStatisticsDto;
import sk.styk.martin.apkanalyzer.facade.AppDataFacade;
import sk.styk.martin.apkanalyzer.rest.exception.ConflictException;
import sk.styk.martin.apkanalyzer.service.UsageStatisticsService;

import javax.inject.Inject;
import java.util.List;

/**
 * Rest controller for usage statistics.
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@RestController
@RequestMapping(value = ApiUris.USAGE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UsageStatisticsEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(UsageStatisticsEndpoint.class);

    @Inject
    private UsageStatisticsService usageStatisticsService;

    @RequestMapping(method = RequestMethod.GET)
    public UsageStatisticsDto get() {
       return usageStatisticsService.getAll();
    }
}