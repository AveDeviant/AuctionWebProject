package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.LotEditImpl;
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
public class LotEditTest {
  private static   HttpServletRequest request;
  private static Command edit;

    @BeforeClass
    public static void init(){
        request = Mockito.mock(HttpServletRequest.class);
        edit = new LotEditImpl();
    }

    @Test
    public void lotEditTestLotDoesntExist(){
        Mockito.when(request.getParameter("id")).thenReturn("1");
        PageResponse pageResponse = edit.execute(request);
        Assert.assertTrue(pageResponse.getResponseType()== ResponseType.FORWARD);
    }
}
