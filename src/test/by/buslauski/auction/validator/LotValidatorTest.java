package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.exception.InvalidDateValueException;
import by.buslauski.auction.validator.LotValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mikita Buslauski
 */
public class LotValidatorTest {


    @Test
    public void checkLotTestInvalidInput() throws InvalidDateValueException {
        String lotTitle = "Example";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "2017-03-03";  //passed date
        boolean actual = LotValidator.checkLot(lotTitle, lotDescription, date);
        Assert.assertEquals(false, actual);
    }

    @Test(expected = InvalidDateValueException.class)
    public void checkLotInvalidDate() throws InvalidDateValueException {
        String lotTitle = "Example";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "06.06.2017";   // date doesn't match to ISO format
        LotValidator.checkLot(lotTitle, lotDescription, date);
    }

    @Test
    public void checkLotShortTitle() throws InvalidDateValueException {
        String lotTitle = "LOL";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    @Test
    public void checkLotNullValues() throws InvalidDateValueException {
        String lotTitle = "Tiiitle";
        String lotDescription = null;
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    @Test
    public void CheckLotNullValues2() throws InvalidDateValueException {
        String lotTitle = null;
        String lotDescription = "description";
        String date = "2017-08-08";
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    /**
     * Note that test was written 06.05.2017
     *
     * @throws InvalidDateValueException if entered date cannot be converted to ISO format.
     */
    @Test
    public void CheckLotCorrectValues() throws InvalidDateValueException {
        String lotTitle = "test lot";
        String lotDescription = "description";
        String date = "2017-06-06";
        Assert.assertTrue(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

    /**
     * This test was written on 2017.05.25
     *
     * @throws InvalidDateValueException if entered date cannot be converted to ISO format.
     */
    @Test
    public void tooLongPeriod() throws InvalidDateValueException {
        String lotTitle = "test lot";
        String lotDescription = "description";
        String date = "2017-08-06";  // max period exceeded.
        Assert.assertFalse(LotValidator.checkLot(lotTitle, lotDescription, date));
    }

}
