package cz.muni.fi.pa165.tracker.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Martin Styk
 * @version 13.11.2016
 */
@Service
public class TimeServiceImpl implements TimeService {

    @Override
    public LocalDateTime timeNow() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime timeWeekAgo() {
        return LocalDateTime.now().minusDays(7);
    }

    @Override
    public LocalDateTime timeMonthAgo() {
        return LocalDateTime.now().minusMonths(1);
    }

    @Override
    public LocalDate dateNow() {
        return timeNow().toLocalDate();
    }

    @Override
    public LocalDate dateWeekAgo() {
        return timeWeekAgo().toLocalDate();
    }

    @Override
    public LocalDate dateMonthAgo() {
        return timeMonthAgo().toLocalDate();
    }
}
