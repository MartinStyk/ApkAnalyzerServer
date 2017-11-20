package cz.muni.fi.pa165.tracker.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;

import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

/**
 * Aspect for automatic translation of exceptions to ActivityTrackerDataAccessException.
 * <p>
 * Every exception from data access layer is translated to {@link ActivityTrackerDataAccessException}.
 * This is effective for all public methods annotated by {@link TranslatePersistenceExceptions} or all public
 * method placed in class annotated by {@link TranslatePersistenceExceptions}.
 *
 * @author Martin Styk
 * @version 08.11.2016
 */
@Aspect
@Named
public class DataAccessExceptionTranslateAspect {

    @Around("(@annotation(TranslatePersistenceExceptions) || @within(TranslatePersistenceExceptions))" +
            "&& execution(public * *(..))")
    public Object translateDataAccessException(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (ConstraintViolationException | PersistenceException | DataAccessException e) {
            throw new ActivityTrackerDataAccessException("Exception in access to data layer on method "
                    + pjp.toShortString(), e);
        }
    }
}
