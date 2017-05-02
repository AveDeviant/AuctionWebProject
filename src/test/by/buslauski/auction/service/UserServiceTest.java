package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Acer on 24.04.2017.
 */
public class UserServiceTest {
    private static UserService userService;

    @BeforeClass
    public static void init() {
        userService = new UserServiceImpl();
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with userName (login) 'AuctionHouse'
     */
    @Test
    public void registerUserNotUniqueName() throws ServiceException {
        User user = userService.registerUser("AuctionHouse", "InvalidInput", "buslauskima@gmail.com","Checking");
        Assert.assertEquals(null, user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with email 'buslauskima@gmail.com'
     */
    @Test
    public void registerUserNotUniqueEmail() throws ServiceException {
        User user = userService.registerUser("Customer88", "invalid Input", "buslauskima@gmail.com","Checking");
        Assert.assertEquals(null, user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with username (login) 'AuctionHouse' and password is "IamAdmin15' (password encrypted)
     */
    @Test
    public void authorizationCheckingInvalidPassword() throws ServiceException {
    User user = userService.authorizationChecking("AuctionHouse", "IncorrectPassword99");
    Assert.assertNull(user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with username (login) 'AuctionHouse' and password is "IamAdmin15' (password encrypted)
     */
    @Test
    public void authorizationCheckingValidData() throws ServiceException {
        User user = userService.authorizationChecking("AuctionHouse", "IamAdmin95");
        Assert.assertNotNull(user);
    }
}
