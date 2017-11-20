package cz.muni.fi.pa165.tracker.spring.mvc.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * @author Martin Styk
 * @version 13.12.2016
 */
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeConverter.class);

    //DD.MM.YYYY HH:mm
    @Override
    public LocalDateTime convert(String input) {
        try {
            logger.debug(input);
            String[] parsed = input.split(" ");
            String date = parsed[0];
            String time = parsed[1];

            String[] dateParsed = date.split("\\.");
            String[] timeParsed = time.split(":");

            LocalDateTime localDate = LocalDateTime.of(Integer.valueOf(dateParsed[2]),
                    Integer.valueOf(dateParsed[1]),
                    Integer.valueOf(dateParsed[0]),
                    Integer.valueOf(timeParsed[0]),
                    Integer.valueOf(timeParsed[1]));
            return localDate;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
