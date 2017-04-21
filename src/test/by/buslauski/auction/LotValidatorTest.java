package test.by.buslauski.auction;

import by.buslauski.auction.validator.LotValidator;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by Acer on 14.04.2017.
 */
public class LotValidatorTest {


    @Test
    public void checkLotTestInvalidInput() {
        String lotTitle = "Example";
        String lotDescription = "Such test, much invalid,  woooow";
        String date = "2017-03-03";
        boolean actual = LotValidator.checkLot(lotTitle, lotDescription, date);
        Assert.assertEquals(false, actual);
    }
}
