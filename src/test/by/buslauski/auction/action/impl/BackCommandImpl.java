package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.PageBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class BackCommandImpl {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static Command command;

    @BeforeClass
    public static void init(){
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        command = new by.buslauski.auction.action.impl.BackCommandImpl();
    }

    @Test
    public void testEmptyQueue(){
        Mockito.when(request.getSession()).thenReturn(session);
        PageBrowser pageBrowser = new PageBrowser();
        Mockito.when(session.getAttribute(SessionAttributes.PAGE_BROWSER)).thenReturn(pageBrowser);
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals("/index.jsp",pageResponse.getPage());
    }

    @Test
    public void testQueueContainsPage(){
        Mockito.when(request.getSession()).thenReturn(session);
        PageBrowser pageBrowser = new PageBrowser();
        String page = "/Auction?command=showLot&id=16";
        pageBrowser.addPageToHistory(page);
        Mockito.when(session.getAttribute(SessionAttributes.PAGE_BROWSER)).thenReturn(pageBrowser);
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals(page,pageResponse.getPage());
    }
}
