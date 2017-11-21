package sk.styk.martin.apkanalyzer.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
public class TimeServiceImpl implements TimeService {

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}

