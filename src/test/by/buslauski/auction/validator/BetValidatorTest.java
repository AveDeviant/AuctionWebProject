package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.BetValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 02.05.2017.
 */
public class BetValidatorTest {

    @Test
    public void checkPriceForValidInvalidInput() {
        String invalidBet = "341f.012";
        Assert.assertFalse(BetValidator.checkPriceForValid(invalidBet));
    }

    @Test
    public void checkPriceForValidOutOfBounds() {
        String invalidBet = "9999999999999999999999999999999999999999999.99";
        Assert.assertFalse(BetValidator.checkPriceForValid(invalidBet));
    }
}
