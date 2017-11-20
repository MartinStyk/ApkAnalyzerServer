package cz.muni.fi.pa165.tracker.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 409 Conflict response code. Used when resource can not be deleted.
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource can not be deleted  ")
public class ResourceCanNotBeDeleted extends RuntimeException {
    public ResourceCanNotBeDeleted(Throwable cause) {
        super(cause);
    }
}