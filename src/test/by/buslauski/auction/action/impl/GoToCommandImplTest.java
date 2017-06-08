package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.GoToCommandImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class GoToCommandImplTest {
    private static HttpServletRequest request;
    private static Command command;

    @BeforeClass
    public static void init(){
        request = Mockito.mock(HttpServletRequest.class);
        command = new GoToCommandImpl();
    }

    @Test
    public void goToTest(){
        Mockito.when(request.getParameter("command")).thenReturn("goTo");
        Mockito.when(request.getParameter("page")).thenReturn("authorization");
        PageResponse pageResponse = command.execute(request);
        Assert.assertTrue(ResponseType.FORWARD==pageResponse.getResponseType());
        Assert.assertEquals(PageNavigation.AUTHORIZATION_PAGE,pageResponse.getPage());
    }

    @Test
    public void goToUnknownPage(){
        Mockito.when(request.getParameter("command")).thenReturn("goTo");
        Mockito.when(request.getParameter("page")).thenReturn("test");
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals(PageNavigation.INDEX_PAGE,pageResponse.getPage());
    }
}
