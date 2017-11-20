package cz.muni.fi.pa165.tracker.spring.mvc.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * @author Martin Styk
 * @version 13.12.2016
 */
public class LocalDateConverter implements Converter<String, LocalDate> {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateConverter.class);

    @Override
    public LocalDate convert(String time) {
        try {
            logger.debug(time);
            String[] parsed = time.split("\\.");
            LocalDate localDate = LocalDate.of(Integer.valueOf(parsed[2]),
                    Integer.valueOf(parsed[1]),
                    Integer.valueOf(parsed[0]));
            return localDate;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
