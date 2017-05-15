package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
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
    private static UserService userService;
    private static Lot unableLot;

    @BeforeClass
    public static void init() {
        lotService = new LotServiceImpl();
        userService = new UserServiceImpl();
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
        User admin = userService.findAdmin();
        lotService.addLot(admin, "Lot Title", "Lot description", "lot image", new BigDecimal(100.00),
                "other", "2017-05-01");
        System.out.println("Test lot added.");
        ArrayList<Lot> lots = lotService.getAllLots();
        Lot addedLot = lots.get(lots.size() - 1);
        long addedLotId = addedLot.getId();
        System.out.println("Deleting lot...");
        lotService.deleteLot(addedLotId,admin);
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
        User admin = userService.findAdmin();
        System.out.println("Trying to delete lot...");
        lotService.deleteLot(lot.getId(),admin);
    }

    /**
     * Note: for successfully test passing database must store lot with ID=16.
     */
    @Test
    public void changeLotBiddingStatusTest() throws ServiceException {
        User admin = userService.findAdmin();
        System.out.println("Removing lot from auction list...");
        lotService.changeLotBiddingStatus(16, false,admin);
        Lot lotAfterFirstOperation = lotService.getLotById(16);
        Assert.assertEquals(false, lotAfterFirstOperation.getAvailability());
        System.out.println("Returning lot to auction list...");
        lotService.changeLotBiddingStatus(16, true,admin);
        Lot lotAfterSecondOperation = lotService.getLotById(16);
        Assert.assertEquals(true, lotAfterSecondOperation.getAvailability());
    }

    @Test
    public void getAllLotsTest() throws ServiceException {
        ArrayList<Lot> lots = lotService.getAllLots();
        Assert.assertTrue(lots.size() > 0);
    }

    /**
     * Note that customer with ID=1 is an administrator of the auction and all lots that
     * he exposes are available (approved) for bidding.
     *
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void findApprovedUserLotsTest() throws ServiceException {
        ArrayList<Lot> lots = lotService.findApprovedUserLots(1);
        Assert.assertTrue(lots.size() > 0);
    }

    /**
     * Note that database stores lot with ID=16.
     *
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void getLotByIdTest() throws ServiceException {
        Lot lot = lotService.getLotById(16);
        Assert.assertNotNull(lot);
    }

    /**
     * Note that database doesn't store the lot with ID=1.
     *
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void getAvailableLotByIdLotDoesntExists() throws ServiceException {
        Lot lot = lotService.getAvailableLotById(1);
        Assert.assertNull(lot);
    }
}