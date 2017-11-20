package cz.muni.fi.pa165.tracker.spring.mvc.validator;

import cz.muni.fi.pa165.tracker.dto.SportActivityCreateDTO;
import cz.muni.fi.pa165.tracker.exception.NonExistingEntityException;
import cz.muni.fi.pa165.tracker.facade.SportActivityFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validate constrain name is unique during sport create.
 *
 * @author Martin Styk
 * @version 7.12.2016
 */
@Named
public class UniqueSportNameCreateValidator implements Validator {

    @Inject
    private SportActivityFacade sportActivityFacade;

    @Override
    public boolean supports(Class<?> clazz) {
        return SportActivityCreateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SportActivityCreateDTO sportDTO = SportActivityCreateDTO.class.cast(target);
        try {
            String name = sportDTO.getName();
            sportActivityFacade.getSportActivityByName(name);
            errors.rejectValue("name", "sport.name_unique");
        } catch (NonExistingEntityException | IllegalArgumentException e) {
            //OK sport doesn't exist
        }
        return;

    }
}
