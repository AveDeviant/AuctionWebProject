package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.GetLotsByCategoryImpl;
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
public class GetLotsByCategoryTest {
    private static HttpServletRequest request;
    private static Command command;

    @BeforeClass
    public static void init(){
        request = Mockito.mock(HttpServletRequest.class);
        command = new GetLotsByCategoryImpl();
    }

    @Test
    public void categoryDoesntExists(){
        Mockito.when(request.getParameter("categoryValue")).thenReturn("Guess, who's back?");
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals(PageNavigation.PAGE_NOT_FOUND,pageResponse.getPage());
    }

    @Test
    public void correctCategory(){
        Mockito.when(request.getParameter("categoryValue")).thenReturn("automobiles");
        PageResponse pageResponse = command.execute(request);
        Assert.assertEquals(PageNavigation.MAIN_PAGE,pageResponse.getPage());
    }
}
