package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.service.BetService;
import by.buslauski.auction.service.impl.BetServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Acer on 12.04.2017.
 */
public class BetServiceTest {
    private static BetService betService;
    private static Lot testLot;

    @BeforeClass
    public static void init() {
        betService = new BetServiceImpl();
        testLot = new Lot(1, 1, "my lot", "description", "image",
                1, new BigDecimal(100.00), true,
                LocalDate.parse("2017-04-02"), new BigDecimal(302.10),
                "test category");
    }

    @Test
    public void checkBetValueTestIncorrectBet() {
        BigDecimal bet = new BigDecimal(302.10);
        boolean actual = betService.checkBetValue(testLot, bet);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void checkBetValueTestCorrectBet() {
        BigDecimal bet = new BigDecimal(350.00);
        boolean actual = betService.checkBetValue(testLot, bet);
        Assert.assertEquals(true, actual);
    }
}
