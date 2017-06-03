package test.by.buslauski.auction.action;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.CommandFactory;
import by.buslauski.auction.action.impl.DeleteLotImpl;
import by.buslauski.auction.action.impl.InitCommandImpl;
import by.buslauski.auction.action.impl.customer.GetUserOperationsImpl;
import by.buslauski.auction.action.impl.customer.TraderRatingImpl;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Deque;

/**
 * Created by Acer on 19.05.2017.
 */
public class FactoryTest {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static CommandFactory factory;
    private static UserService userService;

    @BeforeClass
    public static void init() {
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        factory = new CommandFactory();
        userService = new UserServiceImpl();
    }

    @Test
    public void tryingToAddCategory() {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(null);
        Mockito.when(request.getParameter("command")).thenReturn("addCategory");
        Command command = factory.getCurrentCommand(request);
        Assert.assertTrue(InitCommandImpl.class == command.getClass());
    }

    @Test
    public void tryingToDeleteLot() {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(null);
        Mockito.when(request.getParameter("command")).thenReturn("deleteLot");
        Command command = factory.getCurrentCommand(request);
        Assert.assertTrue(InitCommandImpl.class == command.getClass());
    }

    @Test
    public void adminAccess() throws ServiceException {
        Mockito.when(request.getSession()).thenReturn(session);
        User user = userService.findAdmin();
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(user);
        Mockito.when(request.getParameter("command")).thenReturn("deleteLot");
        Command command = factory.getCurrentCommand(request);
        Assert.assertTrue(DeleteLotImpl.class == command.getClass());
    }

    @Test
    public void authorizedUser() throws ServiceException {
        Mockito.when(request.getSession()).thenReturn(session);
        User user = userService.getAllCustomers().get(0);
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(user);
        Mockito.when(request.getParameter("command")).thenReturn("updateRating");
        Command command = factory.getCurrentCommand(request);
        Assert.assertTrue(TraderRatingImpl.class == command.getClass());
    }

}
