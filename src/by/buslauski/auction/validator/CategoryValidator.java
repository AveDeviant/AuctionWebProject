package by.buslauski.auction.validator;

/**
 * Created by Acer on 10.04.2017.
 */
public class CategoryValidator {

    public static boolean checkCategoryForValid(String name) {
        return name.matches("[A-Za-z А-Яа-я-]{2,}");
    }
}
