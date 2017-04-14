package by.buslauski.auction.validator;


import java.time.LocalDate;

/**
 * Created by Acer on 17.03.2017.
 */
public class LotValidator {
    private static final int MIN_TITLE_LENGTH = 6;
    private static final int MAX_TITLE_LENGTH = 45;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    public static boolean checkLot(String title, LocalDate date) {
        return (checkTitle(title) && checkDate(date));
    }

    public static boolean checkLot(String title, String description, LocalDate date) {
        return (checkTitle(title) && checkDescription(description) && checkDate(date));
    }

    //regexp
    private static boolean checkTitle(String title) {
        return title.length() >= MIN_TITLE_LENGTH && title.length() <= MAX_TITLE_LENGTH;
    }

    private static boolean checkDescription(String description) {
        return description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    private static boolean checkDate(LocalDate checkedDate) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(checkedDate)) {
            return false;
        }
        return true;
    }

}
