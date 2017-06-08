package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.customer.BetCommandImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class BetCommandTest {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static Command betCommand;
    private static UserService userService;


    @BeforeClass
    public static void init() {
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        betCommand = new BetCommandImpl();
        userService = new UserServiceImpl();
    }

    @Test
    public void execute() {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getParameter(SessionAttributes.JSP_PATH)).thenReturn("/Auction?command=showLot&lotId=16");
        Mockito.when(request.getParameter("lotId")).thenReturn("16");
        PageResponse pageResponse = betCommand.execute(request);
        Assert.assertTrue(pageResponse.getResponseType() == ResponseType.FORWARD);
        Assert.assertEquals(pageResponse.getPage(), PageNavigation.AUTHORIZATION_PAGE);
    }

    @Test
    public void betTestLotUnableForBidding() throws ServiceException {
        Mockito.when(request.getRequestURI()).thenReturn("/Auction");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getParameter(SessionAttributes.JSP_PATH)).thenReturn("?command=showLot&lotId=1");
        Mockito.when(request.getParameter("lotId")).thenReturn("1");
        Mockito.when(request.getSession().getAttribute(SessionAttributes.USER)).
                thenReturn(userService.findUserById(4));
        PageResponse pageResponse = betCommand.execute(request);
        Assert.assertTrue(pageResponse.getResponseType() == ResponseType.REDIRECT);
        Assert.assertEquals(pageResponse.getPage(),"/Auction?command=goTo&page=accessDenied");
    }
}

