package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.ShowLotImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.PageResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class ShowLotTest {
    private static HttpServletRequest request;
    private static Command showLot;

    @BeforeClass
    public static void init() {
        request = Mockito.mock(HttpServletRequest.class);
        showLot = new ShowLotImpl();
    }

    @Test
    public void showLotUnableForBidding() {
        Mockito.when(request.getParameter("id")).thenReturn("1");
        PageResponse response = showLot.execute(request);
        Assert.assertEquals(response.getPage(), PageNavigation.PAGE_NOT_FOUND);
    }
}
