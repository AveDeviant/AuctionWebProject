package by.buslauski.auction.validator;

/**
 * @author Buslauski Mikita
 */
public class CategoryValidator {
    private static final String CATEGORY_REGEXP = "[A-Za-z А-Яа-я-;,. ]{2,45}";

    public static boolean checkCategoryForValid(String name) {
        return name.matches(CATEGORY_REGEXP);
    }
}
