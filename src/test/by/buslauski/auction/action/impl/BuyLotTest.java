package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.customer.BuyLotImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 02.06.2017.
 */
public class BuyLotTest {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static UserService userService;
    private static Command command;

    @BeforeClass
    public static void init() {
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        userService = new UserServiceImpl();
        Mockito.when(request.getSession()).thenReturn(session);
        command = new BuyLotImpl();

    }

    @Test
    public void emptyWinningList() throws ServiceException {
        User user = userService.findAdmin();
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(user);
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals(PageNavigation.INDEX_PAGE, pageResponse.getPage());
    }

}
