package by.buslauski.auction.validator;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageValidator {

    public static boolean checkMessage(String text) {
        return (text != null) && (!text.trim().isEmpty());
    }
}
