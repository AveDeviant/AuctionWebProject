package by.buslauski.auction.validator;

/**
 * @author Buslauski Mikita
 */
public class MessageValidator {
    private static final int THEME_LENGTH = 45;

    public static boolean checkMessage(String theme, String text) {
        if (theme == null) {
            return (text != null) && (!text.trim().isEmpty());
        }
        return theme.length() <= THEME_LENGTH && (text != null)
                && (!text.trim().isEmpty());
    }
}
