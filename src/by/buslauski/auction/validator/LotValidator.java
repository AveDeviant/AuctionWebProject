package by.buslauski.auction.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

/**
 * Created by Acer on 17.03.2017.
 */
public class LotValidator {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MIN_TITLE_LENGTH = 6;

    public boolean checkLot(String title, LocalDate date) {
        return (checkTitle(title) && checkDate(date));
    }

    private boolean checkTitle(String title) {
        return title.length() >= MIN_TITLE_LENGTH;
    }

    private boolean checkDate(LocalDate checkedDate) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(checkedDate)) {
            LOGGER.error("Invalid entered date.");
            return false;
        }
        return true;
    }

}
