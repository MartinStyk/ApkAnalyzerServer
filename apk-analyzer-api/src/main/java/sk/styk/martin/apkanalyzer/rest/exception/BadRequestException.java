package sk.styk.martin.apkanalyzer.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 404 Bad Request code.
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadRequestException extends RuntimeException {
    public BadRequestException(Throwable cause) {
        super(cause);
    }
}