package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.BankService;
import by.buslauski.auction.service.impl.BankServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Acer on 29.04.2017.
 */
public class BankServiceTest {
    private static BankService bankService;

    @BeforeClass
    public static void init() {
        bankService = new BankServiceImpl();
    }

    /**
     * Note: for successfully test passing database must store account with "MasterCard" system and
     * card number 1122-3344-5566-7788
     */
    @Test
    public void registerCardNonUniqueInput() throws ServiceException {
        BankCard card = bankService.addAccount(1, "MasterCard", "1122-3344-5566-7788");
        Assert.assertEquals(null, card);
    }
}
