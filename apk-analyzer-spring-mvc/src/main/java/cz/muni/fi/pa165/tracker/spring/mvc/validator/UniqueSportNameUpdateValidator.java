package cz.muni.fi.pa165.tracker.spring.mvc.validator;

import cz.muni.fi.pa165.tracker.dto.SportActivityUpdateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.SportActivityFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validate constrain name is unique during sport update.
 *
 * @author Martin Styk
 * @version 7.12.2016
 */
@Named
public class UniqueSportNameUpdateValidator implements Validator {

    @Inject
    private SportActivityFacade sportActivityFacade;

    @Override
    public boolean supports(Class<?> clazz) {
        return SportActivityUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SportActivityUpdateDTO sportDTO = SportActivityUpdateDTO.class.cast(target);
        String name = sportDTO.getName();
        String previousName = sportActivityFacade.getSportActivityById(sportDTO.getId()).getName();

        //if name is not changed, we are fine
        if (previousName.equals(name)) {
            return;
        }

        //if name is changed, we need to check whether it is unique
        try {
            sportActivityFacade.getSportActivityByName(name);
            errors.rejectValue("name", "sport.name_unique_update");
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            //OK sport doesn't exist
        }
        return;

    }
}
