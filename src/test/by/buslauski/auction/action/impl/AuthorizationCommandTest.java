package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.AuthorizationCommandImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.ResponseType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Created by Acer on 16.05.2017.
 */
public class AuthorizationCommandTest {
private static HttpServletRequest request;
private static Command authorization;
private static HttpSession session;


@BeforeClass
public static void init(){
    request = Mockito.mock(HttpServletRequest.class);
    session = Mockito.mock(HttpSession.class);
    authorization = new AuthorizationCommandImpl();
}

    @Test
    public void executeTestInvalidPassword() {
        Mockito.when(request.getParameter("login")).thenReturn("AuctionHouse");
        Mockito.when(request.getParameter("password")).thenReturn("IamAdmin99");
        Assert.assertTrue(authorization.execute(request).getResponseType()== ResponseType.FORWARD);
    }

    @Test
    public void executeTestAuthorizationPassedSuccessfully(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("login")).thenReturn("AuctionHouse");
        Mockito.when(request.getParameter("password")).thenReturn("IamAdmin95");
        Mockito.when(request.getSession()).thenReturn(session);
        Assert.assertTrue(authorization.execute(request).getResponseType()== ResponseType.REDIRECT);
        Assert.assertEquals(authorization.execute(request).getPage(), PageNavigation.INDEX_PAGE);
    }


}
