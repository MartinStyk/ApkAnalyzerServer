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
import sk.styk.martin.apkanalyzer.entity.AppData;
import sk.styk.martin.apkanalyzer.facade.AppDataFacade;
import sk.styk.martin.apkanalyzer.rest.exception.ConflictException;

import javax.inject.Inject;
import java.util.List;

/**
 * Rest controller for app data.
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@RestController
@RequestMapping(value = ApiUris.APPS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AppDataEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(AppDataEndpoint.class);

    @Inject
    private AppDataFacade appDataFacade;

    @RequestMapping(method = RequestMethod.GET)
    public List<AppDataDto> get(@RequestParam(value = "device", required = false) String deviceId) {
        List<AppDataDto> data;
        if (deviceId == null) {
            data = appDataFacade.findAll();
        } else {
            data = appDataFacade.findByDevice(deviceId);
        }
        return data;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppDataDto get(@PathVariable("id") long id) {
        return appDataFacade.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppDataDto create(@RequestBody AppDataDto data) {
        AppDataDto created = appDataFacade.createWithExistenceCheck(data);
        if (created != null) {
            return created;
        } else {
            throw new ConflictException();
        }
    }
}