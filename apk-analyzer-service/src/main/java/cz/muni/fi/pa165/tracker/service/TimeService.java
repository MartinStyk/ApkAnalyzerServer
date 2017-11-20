package cz.muni.fi.pa165.tracker.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service for providing date time data.
 *
 * @author Martin Styk
 * @version 13.11.2016
 */
public interface TimeService {

    /**
     * @return current time
     */
    LocalDateTime timeNow();

    /**
     * @return time week ago
     */
    LocalDateTime timeWeekAgo();

    /**
     * @return time month ago
     */
    LocalDateTime timeMonthAgo();

    /**
     * @return current date
     */
    LocalDate dateNow();

    /**
     * @return date week ago
     */
    LocalDate dateWeekAgo();

    /**
     * @return date month ago
     */
    LocalDate dateMonthAgo();
}
