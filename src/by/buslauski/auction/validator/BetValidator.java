package by.buslauski.auction.validator;

import by.buslauski.auction.validator.exception.InvalidNumberValueException;

import java.math.BigDecimal;

/**
 * @author Buslauski Mikita
 */
public class BetValidator {
    private static final String PRICE_REGEXP = "^[1-9][0-9]*.[0-9]{2}";
    private static final BigDecimal MAX_PRICE = new BigDecimal(99999999.99);

    public static boolean checkPriceForValid(String price) throws InvalidNumberValueException {
        if (price.matches(PRICE_REGEXP)) {
            BigDecimal enteredPrice = initPrice(price);
            if (MAX_PRICE.compareTo(enteredPrice) < 0) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static BigDecimal initPrice(String bet) throws InvalidNumberValueException {
        BigDecimal price;
        bet = bet.replaceAll(",",".");
        try {
            price = new BigDecimal(bet);
        } catch (NumberFormatException e) {
            throw new InvalidNumberValueException(e);
        }
        return price;
    }
}
