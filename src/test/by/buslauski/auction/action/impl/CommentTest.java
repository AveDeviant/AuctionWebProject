package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.CommentImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
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
public class CommentTest {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static Command comment;
    private static UserService userService;

    @BeforeClass
    public static void init(){
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        comment = new CommentImpl();
        userService = new UserServiceImpl();
    }

    @Test
    public void testLotUnable() throws ServiceException {
        User admin = userService.findAdmin();
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getParameter("lotId")).thenReturn("1");
        Mockito.when(request.getAttribute(SessionAttributes.JSP_PATH)).thenReturn("?command=showLot&id=1");
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(admin);
        PageResponse pageResponse = comment.execute(request);
        Assert.assertTrue(pageResponse.getResponseType()== ResponseType.FORWARD);
        Assert.assertEquals(PageNavigation.PAGE_NOT_FOUND,pageResponse.getPage());
    }
}
