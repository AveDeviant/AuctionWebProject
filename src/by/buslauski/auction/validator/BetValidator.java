package by.buslauski.auction.validator;

/**
 * Created by Acer on 11.04.2017.
 */
public class BetValidator {
    private static final String PRICE_REGEXP = "^[1-9][0-9]*.[0-9]{2}";

    public static boolean checkPriceForValid(String price) {
        return price.matches(PRICE_REGEXP);
    }
}
