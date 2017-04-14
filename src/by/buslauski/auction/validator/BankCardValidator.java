package by.buslauski.auction.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acer on 20.03.2017.
 */
public class BankCardValidator {
    private static final String CARD_REGEXP = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";


    public boolean checkCardForValidNumber(String number) {
        Pattern pattern = Pattern.compile(CARD_REGEXP);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
