package by.buslauski.auction.validator;

/**
 * Created by Acer on 10.04.2017.
 */
public class CategoryValidator {
    private static final String CATEGORY_REGEXP = "[A-Za-z А-Яа-я-;,. ]{2,45}";

    public static boolean checkCategoryForValid(String name) {
        return name.matches(CATEGORY_REGEXP);
    }
}
