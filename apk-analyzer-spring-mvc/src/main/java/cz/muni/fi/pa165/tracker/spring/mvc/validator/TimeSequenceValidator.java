package cz.muni.fi.pa165.tracker.spring.mvc.validator;

import cz.muni.fi.pa165.tracker.dto.ActivityReportCreateDTO;
import cz.muni.fi.pa165.tracker.facade.SportActivityFacade;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;

/**
 * Validate constrain time sequence of LocalDateTimes.
 *
 * @author Martin Styk
 * @version 13.12.2016
 */
public class TimeSequenceValidator implements Validator {

    @Inject
    private SportActivityFacade sportActivityFacade;

    @Override
    public boolean supports(Class<?> clazz) {
        return ClassUtils.isAssignable(clazz, ActivityReportCreateDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ActivityReportCreateDTO activityReportDTO = ActivityReportCreateDTO.class.cast(target);

        if (activityReportDTO.getEndTime() != null &&
                activityReportDTO.getEndTime().isAfter(activityReportDTO.getStartTime())) {
            return;
        }

        errors.rejectValue("startTime", "reports.time_sequence");
        errors.rejectValue("endTime", "reports.time_sequence");
    }
}
