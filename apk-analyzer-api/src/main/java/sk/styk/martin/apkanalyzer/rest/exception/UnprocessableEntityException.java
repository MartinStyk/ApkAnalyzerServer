package sk.styk.martin.apkanalyzer.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used by Rest services.
 * Represents HTTP 422 Unprocessable entity response code
 *
 * @author Martin Styk
 * @version 21.11.2017
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Unprocessable entity")
public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(Throwable cause) {
        super(cause);
    }
}
