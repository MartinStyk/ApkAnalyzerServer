package sk.styk.martin.apkanalyzer.service;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

/**
 * @author Martin Styk
 * @version 11.11.2017
 */
@ApplicationScoped
public class TimeService {

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
