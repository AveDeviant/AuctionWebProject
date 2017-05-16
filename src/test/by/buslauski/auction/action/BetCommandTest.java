package test.by.buslauski.auction.action;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.customer.BetCommandImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 16.05.2017.
 */
public class BetCommandTest {

    @Test
    public void execute() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Command betCommand = new BetCommandImpl();
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getParameter(SessionAttributes.JSP_PATH)).thenReturn("?command=showLot&lotId=16");
        Mockito.when(request.getParameter("lotId")).thenReturn("16");
        PageResponse pageResponse = betCommand.execute(request);
        Assert.assertTrue(pageResponse.getResponseType() == ResponseType.FORWARD);
        Assert.assertEquals(pageResponse.getPage(), PageNavigation.AUTHORIZATION_PAGE);
    }
}

