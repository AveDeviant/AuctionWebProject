package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.BankCardValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 02.05.2017.
 */
public class BankCardValidatorTest {

    @Test
    public void checkCardForValidNumberTestInvalidInput() {
        String invalidNumber1 = "12053331-3315-0031";
        String invalidNumber2 = "3306-216-3401-0001";
        String invalidNumber3 = "5410-4412-90f1-5513";
        boolean[] expected = new boolean[]{false, false, false};
        boolean[] actual = new boolean[]{BankCardValidator.checkCardForValidNumber(invalidNumber1),
                BankCardValidator.checkCardForValidNumber(invalidNumber2),
                BankCardValidator.checkCardForValidNumber(invalidNumber3)};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void checkCardForValidNumberCorrectInput() {
        String correctNumber = "1111-2222-3333-4444";
        Assert.assertTrue(BankCardValidator.checkCardForValidNumber(correctNumber));
    }
}
