package sk.styk.martin.apkanalyzer.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 404 Not found response code
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found")
public class NotFoundException extends RuntimeException {
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
