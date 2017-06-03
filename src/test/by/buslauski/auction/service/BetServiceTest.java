package test.by.buslauski.auction.service;

import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.dao.impl.BetDaoImpl;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.BetService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.impl.BetServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Mikita Buslauski
 */
public class BetServiceTest {
    private static BetService betService;
    private static LotService lotService;
    private static UserService userService;
    private static BetDao betDao;
    private static DaoHelper daoHelper;
    private static Lot testLot;

    @BeforeClass
    public static void init() {
        betService = new BetServiceImpl();
        lotService = new LotServiceImpl();
        userService = new UserServiceImpl();
        betDao = new BetDaoImpl();
        daoHelper = new DaoHelper();
        testLot = new Lot(1, 1, "my lot", "description", "image",
                1, new BigDecimal(100.00), true,
                LocalDate.parse("2017-04-02"), new BigDecimal(302.10),
                "test category");   // current lot price is 302.10
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

    @Test
    public void checkBetValueTestInvalidBetStep() {
        BigDecimal bet = new BigDecimal(310.00);
        Assert.assertTrue(betService.checkBetValue(testLot, bet));
    }

    /**
     * Note that after each test run primary key of "lot" table will increase by one.
     *
     * @throws ServiceException in case DAOException has been thrown
     * @throws DAOException     in case SQLException has been thrown.
     */
    @Test
    public void checkBetOperationCorrectBehavior() throws ServiceException, DAOException {
        User admin = userService.findAdmin();
        lotService.addLot(admin, "test", "test", "test", new BigDecimal(100.00), "other", "2017-08-08");
        Lot test = lotService.getAllLots().get(0);
        BigDecimal bet = new BigDecimal(110.00);
        betService.addBet(admin.getUserId(), test.getId(), bet);
        Lot testAfterUpdate = lotService.getLotById(test.getId());
        Assert.assertTrue(bet.compareTo(testAfterUpdate.getCurrentPrice()) == 0); //comparing bet and lot price after updating.
        System.out.println("Deleting bet....");
        try {
            daoHelper.initDao(betDao);
            betDao.resetBets(test.getId());  // delete bet before deleting lot to avoid DAOException
            System.out.println("Deleting lot....");
            lotService.deleteLot(testAfterUpdate.getId(), admin);
        } finally {
            daoHelper.release();
        }
    }
}