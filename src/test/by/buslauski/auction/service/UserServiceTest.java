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
     * with userName 'HeadAdmin'
     */
    @Test
    public void registerUserNotUniqueName() throws ServiceException {
        User user = userService.registerUser("HeadAdmin", "InvalidInput", "buslauskima@gmail.com");
        Assert.assertEquals(null, user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with email 'buslauskima@gmail.com'
     */
    @Test
    public void registerUserNotUniqueEmail() throws ServiceException {
        User user = userService.registerUser("Customer88", "invalidinout", "buslauskima@gmail.com");
        Assert.assertEquals(null, user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with username 'HeadAdmin' and password is "IamAdmin15' (password encrypted)
     */
    @Test
    public void authorizationCheckingInvalidPassword() throws ServiceException {
    User user = userService.authorizationChecking("HeadAdmin", "IncorrectPassword99");
    Assert.assertEquals(null,user);
    }

    /**
     * Note that for successfully test passing the database must stores user
     * with username 'HeadAdmin' and password is "IamAdmin15' (password encrypted)
     */
    @Test
    public void authorizationCheckingValidData() throws ServiceException {
        User user = userService.authorizationChecking("HeadAdmin", "IamAdmin95");
        Assert.assertEquals("HeadAdmin",user.getUserName());
    }
}
