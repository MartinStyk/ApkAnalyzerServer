package cz.muni.fi.pa165.tracker.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class or method annotated by this annotation will be affected by {@link DataAccessExceptionTranslateAspect}.
 *
 * @author Martin Styk
 * @version 17.11.2016
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslatePersistenceExceptions {
}
