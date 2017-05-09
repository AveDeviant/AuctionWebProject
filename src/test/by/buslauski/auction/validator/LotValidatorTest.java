package test.by.buslauski.auction.validator;

import by.buslauski.auction.exception.InvalidInputValueException;
import by.buslauski.auction.validator.LotValidator;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by Acer on 14.04.2017.
 */
public class LotValidatorTest {


    @Test
    public void checkLotTestInvalidInput() throws InvalidInputValueException {
        String lotTitle = "Example";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "2017-03-03";  //passed date
        boolean actual = LotValidator.checkLot(lotTitle, lotDescription, date);
        Assert.assertEquals(false, actual);
    }

    @Test(expected = InvalidInputValueException.class)
    public void checkLotInvalidDate() throws InvalidInputValueException {
        String lotTitle = "Example";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "06.06.2017";   // date doesn't match to ISO format
        LotValidator.checkLot(lotTitle, lotDescription, date);
    }

    @Test
    public void checkLotShortTitle() throws InvalidInputValueException {
        String lotTitle = "LOL";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    @Test
    public void checkLotNullValues() throws InvalidInputValueException {
        String lotTitle = "Tiiitle";
        String lotDescription = null;
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    @Test
    public void CheckLotNullValues2() throws InvalidInputValueException {
        String lotTitle = null;
        String lotDescription = "description";
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    /**
     * Note that test was written 06.05.2017
     *
     * @throws InvalidInputValueException if entered date doesn't appropriate to ISO format.
     */
    @Test
    public void CheckLotNullValues3() throws InvalidInputValueException {
        String lotTitle = "test lot";
        String lotDescription = "description";
        String date = "2017-06-06";
        Assert.assertTrue(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

}
