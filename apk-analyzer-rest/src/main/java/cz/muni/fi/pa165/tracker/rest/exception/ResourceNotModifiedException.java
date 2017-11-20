package cz.muni.fi.pa165.tracker.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 304 Not modified response code.
 * Intended to use with PUT update methods.
 *
 * @author Martin Styk
 * @version 30.11.2016
 */
@ResponseStatus(value = HttpStatus.NOT_MODIFIED, reason = "The requested resource was not modified")
public class ResourceNotModifiedException extends RuntimeException {
    public ResourceNotModifiedException(Throwable cause) {
        super(cause);
    }
}