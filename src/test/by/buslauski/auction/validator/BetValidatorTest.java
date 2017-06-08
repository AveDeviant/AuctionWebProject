package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.exception.InvalidNumberValueException;
import by.buslauski.auction.validator.BetValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mikita Buslauski
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

    @Test
    public void checkPriceForValidValidPrice1() throws InvalidNumberValueException {
        String value = "777,00";
        Assert.assertTrue(BetValidator.checkPriceForValid(value));
    }

    @Test
    public void checkPriceForValidValidPrice2() throws InvalidNumberValueException {
        String validPrice = "100.99";
        Assert.assertTrue(BetValidator.checkPriceForValid(validPrice));
    }

    @Test(expected = InvalidNumberValueException.class)
    public void checkPriceForValidException() throws InvalidNumberValueException {
        String invalidValue = "777и00";
        BetValidator.checkPriceForValid(invalidValue);
    }
}
