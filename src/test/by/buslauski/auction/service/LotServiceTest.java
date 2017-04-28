package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Acer on 12.04.2017.
 */
public class LotServiceTest {
    private static LotService lotService;
    private static Lot unableLot;

    @BeforeClass
    public static void init() {
        lotService = new LotServiceImpl();
        unableLot = new Lot(1, 1, "Lot", "description", "image",
                1, new BigDecimal(100.00), true,
                LocalDate.parse("2017-04-02"), new BigDecimal(100.00),
                "test category");
    }

    @Test
    public void checkActualityTestUnableLot() {
        boolean actual = lotService.checkActuality(unableLot);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void checkAuctionWaitingPeriodTimeOver() {
        boolean actual = lotService.checkWaitingPeriod(unableLot);
        Assert.assertEquals(false, actual);
    }
}
