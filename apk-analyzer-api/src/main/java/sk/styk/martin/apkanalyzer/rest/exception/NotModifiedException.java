package sk.styk.martin.apkanalyzer.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 304 Not modified response code.
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@ResponseStatus(value = HttpStatus.NOT_MODIFIED, reason = "Not modified")
public class NotModifiedException extends RuntimeException {
    public NotModifiedException(Throwable cause) {
        super(cause);
    }
}