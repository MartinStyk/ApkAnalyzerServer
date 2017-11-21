package sk.styk.martin.apkanalyzer.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 409 Conflict response code.
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Conflict")
public class ConflictException extends RuntimeException {

    public ConflictException() {
        super();
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }
}