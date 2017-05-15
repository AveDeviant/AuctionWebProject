package by.buslauski.auction.validator;


import by.buslauski.auction.exception.InvalidDateValueException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author Buslauski Mikita
 */
public class LotValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final String LOT_TITLE_REGEXP = "[\\w\\s\"-,!().'А-Яа-я-]{6,45}";

    /**
     * Check entered lot data for valid.
     * Invokes during lot editing.
     *
     * @param title lot title.
     * @param date  lot bidding date.
     * @return true - entered lot data are correct;
     * false - in other case.
     * @throws InvalidDateValueException in case entered lot bidding date can not be converted
     *                                   to <code>LocalDate</code> object
     * @see by.buslauski.auction.action.impl.LotEditImpl#execute(HttpServletRequest)
     */
    public static boolean checkLot(String title, String date) throws InvalidDateValueException {
        return (checkTitle(title) && checkDate(date));
    }

    /**
     * Check entered lot data for valid.
     * Invokes during adding lot into database.
     *
     * @param title       lot title.
     * @param description lot description.
     * @param date        lot bidding date.
     * @return true - entered lot data are correct.
     * false - in other case.
     * @throws InvalidDateValueException in case entered date can not be converted
     *                                   to <code>LocalDate</code> object.
     * @see by.buslauski.auction.action.impl.AddLotImpl#execute(HttpServletRequest)
     */
    public static boolean checkLot(String title, String description, String date) throws InvalidDateValueException {
        return (checkTitle(title) && checkDescription(description) && checkDate(date));
    }

    private static boolean checkTitle(String title) {
        return title != null && title.matches(LOT_TITLE_REGEXP);
    }

    private static boolean checkDescription(String description) {
        return (description != null) && (!description.isEmpty()) && (description.length() <= MAX_DESCRIPTION_LENGTH);
    }

    /**
     * Checking for compliance with the ISO format and check that entered date later than
     * current date.
     *
     * @param checkedDate entered lot date.
     * @return true - entered date appropriate to ISO format and later than current date;
     * false - entered date doesn't appropriate to ISO format or date passed.
     * @throws InvalidDateValueException in case entered date can not be converted  to
     *                                   <code>LocalDate</code>
     */
    private static boolean checkDate(String checkedDate) throws InvalidDateValueException {
        if (checkedDate != null && !checkedDate.isEmpty()) {
            LocalDate date;
            try {
                date = LocalDate.parse(checkedDate);
            } catch (DateTimeParseException e) {
                throw new InvalidDateValueException(e);
            }
            LocalDate now = LocalDate.now();
            return now.isBefore(date);
        }
        return false;
    }
}
