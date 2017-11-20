package cz.muni.fi.pa165.tracker.spring.mvc.controllers;

import cz.muni.fi.pa165.tracker.dto.UserDTO;
import cz.muni.fi.pa165.tracker.facade.UserFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * Parent controller for all application controllers
 */
public class ActivityTrackerController {

    @Inject
    private UserFacade userFacade;

    @ModelAttribute("loggedUser")
    protected UserDTO getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String name = authentication.getName();

        if (name == null || name.equals("anonymousUser")) {
            return null;
        }

        UserDTO user = userFacade.findUserByEmail(name);
        return user;
    }

    @ModelAttribute("isAdmin")
    protected boolean isUserAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }

    protected void addValidationErrors(BindingResult bindingResult, Model model) {
        for (FieldError fe : bindingResult.getFieldErrors()) {
            model.addAttribute(fe.getField() + "_error", true);
        }
    }

    protected String getUtf8RequestParam(String iso8859String) {
        String utf8String = null;

        if (iso8859String != null) {
            byte[] bytes = iso8859String.getBytes(StandardCharsets.ISO_8859_1);
            utf8String = new String(bytes, StandardCharsets.UTF_8);
        }

        return utf8String;
    }

}

