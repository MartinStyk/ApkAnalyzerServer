package cz.muni.fi.pa165.tracker.spring.mvc.validator;

import cz.muni.fi.pa165.tracker.dto.TeamDTO;
import cz.muni.fi.pa165.tracker.dto.TeamUpdateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.TeamFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validate constrain name is unique during team update.
 *
 * @author Jan Grundmann
 * @version 8.12.2016
 */
@Named
public class UniqueTeamNameUpdateValidator implements Validator {

    @Inject
    private TeamFacade teamFacade;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeamUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeamDTO teamDTO = TeamDTO.class.cast(target);
        String name = teamDTO.getName();
        String previousName = teamFacade.getTeamById(teamDTO.getId()).getName();

        //if name is not changed, we are fine
        if (previousName.equals(name)) {
            return;
        }

        //if name is changed, we need to check whether it is unique
        try {
            teamFacade.getTeamByName(name);
            errors.rejectValue("name", "team.name_unique_update");
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            //OK team doesn't exist
        }
        return;
    }
}
