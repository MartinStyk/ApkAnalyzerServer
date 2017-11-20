package cz.muni.fi.pa165.tracker.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 422 Unprocessable entity response code
 * Used when validation on input params fails
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Resource is invalid")
public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(Throwable cause) {
        super(cause);
    }
}
