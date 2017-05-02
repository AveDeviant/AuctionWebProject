package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public void checkAuctionWaitingPeriodTimeOver() {
        boolean actual = lotService.checkWaitingPeriod(unableLot);
        Assert.assertEquals(false, actual);
    }

    /**
     * Note that after each test run the primary key of "lot" table will increase by one.
     */
    @Test
    public void deleteLotTestSuccessOperation() throws ServiceException {
        System.out.println("Adding test lot...");
        lotService.addLot("Lot Title", 1, "Lot description", "lot image", new BigDecimal(100.00),
                true, "other", "2017-05-01");
        System.out.println("Test lot added.");
        ArrayList<Lot> lots = lotService.getAllLots();
        Lot addedLot = lots.get(lots.size() - 1);
        long addedLotId = addedLot.getId();
        System.out.println("Deleting lot...");
        lotService.deleteLot(addedLotId);
        System.out.println("Lot has been deleted.");
        Lot deletedLot = lotService.getLotById(addedLotId);
        Assert.assertEquals(null, deletedLot);
    }

    /**
     * Note: for successfully test passing database must store lot with ID=16 and
     * this lot must have confirmed bets and/or orders.
     */
    @Test(expected = ServiceException.class)
    public void deleteLotTestHavingBetsOrders() throws ServiceException {
        System.out.println("Getting lot with ID=16");
        Lot lot = lotService.getLotById(16);
        System.out.println("Trying to delete lot...");
        lotService.deleteLot(lot.getId());
    }

    /**
     * Note: for successfully test passing database must store lot with ID=16.
     */
    @Test
    public void changeLotBiddingStatusTest() throws ServiceException {
        System.out.println("Removing lot from auction list...");
        lotService.changeLotBiddingStatus(16, false);
        Lot lotAfterFirstOperation = lotService.getLotById(16);
        Assert.assertEquals(false, lotAfterFirstOperation.getAvailability());
        System.out.println("Returning lot to auction list...");
        lotService.changeLotBiddingStatus(16, true);
        Lot lotAfterSecondOperation = lotService.getLotById(16);
        Assert.assertEquals(true, lotAfterSecondOperation.getAvailability());
    }
}