package cz.muni.fi.pa165.tracker.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 404 Not found response code
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "We are sorry, but the requested resource was not found")
public class RequestedResourceNotFoundException extends RuntimeException {
    public RequestedResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
