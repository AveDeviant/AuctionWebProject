package test.by.buslauski.auction.validator;

import by.buslauski.auction.exception.InvalidNumberValueException;
import by.buslauski.auction.validator.BetValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 02.05.2017.
 */
public class BetValidatorTest {

    @Test
    public void checkPriceForValidInvalidInput() throws InvalidNumberValueException {
        String invalidBet = "341f.012";
        Assert.assertFalse(BetValidator.checkPriceForValid(invalidBet));
    }

    @Test
    public void checkPriceForValidOutOfBounds() throws InvalidNumberValueException {
        String invalidBet = "9999999999999999999999999999999999999999999.99";
        Assert.assertFalse(BetValidator.checkPriceForValid(invalidBet));
    }

    @Test(expected = InvalidNumberValueException.class)
    public void checkPriceFirValid() throws InvalidNumberValueException {
        String invalidValue = "777,00";
        BetValidator.checkPriceForValid(invalidValue);
    }

    @Test
    public void checkPriceForValidValidPrice() throws InvalidNumberValueException {
        String validPrice = "100.99";
        Assert.assertTrue(BetValidator.checkPriceForValid(validPrice));
    }
}
