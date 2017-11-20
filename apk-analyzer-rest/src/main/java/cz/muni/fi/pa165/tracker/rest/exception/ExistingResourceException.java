package cz.muni.fi.pa165.tracker.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 422 Unprocessable entity response code
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Requested resource already exists")
public class ExistingResourceException extends RuntimeException {
    public ExistingResourceException(Throwable cause) {
        super(cause);
    }
}
