package by.buslauski.auction.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Buslauski Mikita
 */
public class DateTimeParser {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime parseDate(String date) {
        if (date != null) {
            date = date.substring(0, date.length() - 2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            return localDateTime;
        }
        return null;
    }
}