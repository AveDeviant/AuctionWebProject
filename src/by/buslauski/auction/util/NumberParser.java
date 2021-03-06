package by.buslauski.auction.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Buslauski Mikita
 */
public class NumberParser {
    private static final Logger LOGGER = LogManager.getLogger();

    public static long parse(String parameter) {
        long value = 0;
        try {
            value = Long.parseLong(parameter);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return value;
    }
}
