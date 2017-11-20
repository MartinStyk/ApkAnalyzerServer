/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pa165.tracker.spring.mvc.controllers;

import cz.muni.fi.pa165.tracker.facade.UserFacade;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Adam Laurenčík
 */
@Controller
public class DefaultController extends ActivityTrackerController {

    @Inject
    private UserFacade userFacade;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        if (getLoggedUser() != null) {
            model.addAttribute("statistics", userFacade.getStatistics(getLoggedUser()));
        }
        return "index";
    }
}
