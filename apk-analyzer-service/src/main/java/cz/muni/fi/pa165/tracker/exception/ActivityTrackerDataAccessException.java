package cz.muni.fi.pa165.tracker.exception;

import org.springframework.dao.DataAccessException;

/**
 * Exception representing unexpected situation on access to data layer of application.
 *
 * @author Martin Styk
 * @version 08.11.2016
 */
public class ActivityTrackerDataAccessException extends DataAccessException {

    public ActivityTrackerDataAccessException(String message) {
        super(message);
    }

    public ActivityTrackerDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
