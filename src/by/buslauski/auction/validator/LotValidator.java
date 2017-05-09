package by.buslauski.auction.validator;


import by.buslauski.auction.exception.InvalidInputValueException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by Acer on 17.03.2017.
 */
public class LotValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final String LOT_TITLE_REGEXP = "[\\w\\s\"-,!().'А-Яа-я-]{6,45}";

    //during editing lot
    public static boolean checkLot(String title, String date) throws InvalidInputValueException {
        return (checkTitle(title) && checkDate(date));
    }

    //during adding lot
    public static boolean checkLot(String title, String description, String date) throws InvalidInputValueException {
        return (checkTitle(title) && checkDescription(description) && checkDate(date));
    }

    private static boolean checkTitle(String title) {
        return title != null && title.matches(LOT_TITLE_REGEXP);
    }

    private static boolean checkDescription(String description) {
       return  (description != null) && (!description.isEmpty()) &&(description.length() <= MAX_DESCRIPTION_LENGTH);
    }

    /**
     * Checking for compliance with the ISO format and check that entered date later than
     * current date.
     *
     * @param checkedDate entered lot date.
     * @return true - entered date appropriate to ISO format and later than current date;
     * false - entered date doesn't appropriate to ISO format or date passed.
     * @throws InvalidInputValueException in case entered date can not be converted  to
     *                                    <code>LocalDate</code>
     */
    private static boolean checkDate(String checkedDate) throws InvalidInputValueException {
        if (checkedDate != null && !checkedDate.isEmpty()) {
            LocalDate date;
            try {
                date = LocalDate.parse(checkedDate);
            } catch (DateTimeParseException e) {
                throw new InvalidInputValueException(e);
            }
            LocalDate now = LocalDate.now();
            return now.isBefore(date);
        }
        return false;
    }
}
