package by.buslauski.auction.validator;


import java.time.LocalDate;

/**
 * Created by Acer on 17.03.2017.
 */
public class LotValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final String LOT_TITLE_REGEXP = "[\\w\\s\"-,!().'А-Яа-я-]{6,45}";

    public static boolean checkLot(String title, String date) {
        return (checkTitle(title) && checkDate(date));
    }

    public static boolean checkLot(String title, String description, String date) {
        return (checkTitle(title) && checkDescription(description) && checkDate(date));
    }

    private static boolean checkTitle(String title) {
        return title.matches(LOT_TITLE_REGEXP);
    }

    private static boolean checkDescription(String description) {
        return description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    private static boolean checkDate(String checkedDate) {
        LocalDate date =LocalDate.parse(checkedDate);
        LocalDate now = LocalDate.now();
        return now.isBefore(date);

    }

}
