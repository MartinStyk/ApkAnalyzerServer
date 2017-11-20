package cz.muni.fi.pa165.tracker.spring.mvc.validator;

import cz.muni.fi.pa165.tracker.dto.TeamCreateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.TeamFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validate constrain name is unique during team create.
 *
 * @author Jan Grundmann
 * @version 8.12.2016
 */
@Named
public class UniqueTeamNameCreateValidator implements Validator {

    @Inject
    private TeamFacade teamFacade;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeamCreateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeamCreateDTO teamDTO = TeamCreateDTO.class.cast(target);
        try {
            String name = teamDTO.getName();
            teamFacade.getTeamByName(name);
            errors.rejectValue("name", "team.name_unique");
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            //OK team doesn't exist
        }
        return;
    }
}
