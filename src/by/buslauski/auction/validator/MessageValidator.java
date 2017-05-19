package by.buslauski.auction.validator;

/**
 * @author Buslauski Mikita
 */
public class MessageValidator {
    private static final int THEME_LENGTH = 45;
    private static final int COMMENT_LENGTH = 140;

    public static boolean checkMessage(String theme, String text) {
        if (theme == null) {
            return (text != null) && (!text.trim().isEmpty());
        }
        return theme.length() <= THEME_LENGTH && (text != null)
                && (!text.trim().isEmpty());
    }

    public static boolean checkComment(String content) {
        return (content != null && !content.trim().isEmpty() && content.length() <= COMMENT_LENGTH);
    }
}
