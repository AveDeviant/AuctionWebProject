package test.by.buslauski.auction.action;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.CommandFactory;
import by.buslauski.auction.action.impl.InitCommandImpl;
import by.buslauski.auction.constant.SessionAttributes;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 19.05.2017.
 */
public class FactoryTest {
    //    private static HttpServletRequest requestUser;
    private static HttpServletRequest requestNonAuthorizedUser;
    private static HttpSession session;
    private static CommandFactory factory;

    @BeforeClass
    public static void init() {
//        requestUser = Mockito.mock(HttpServletRequest.class);
        requestNonAuthorizedUser = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        factory = new CommandFactory();
    }

    @Test
    public void tryingToAddCategory() {
        Mockito.when(requestNonAuthorizedUser.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(SessionAttributes.USER)).thenReturn(null);
        Mockito.when(requestNonAuthorizedUser.getParameter("command")).thenReturn("addCategory");
        Command command = factory.getCurrentCommand(requestNonAuthorizedUser);
        Assert.assertTrue(InitCommandImpl.class == command.getClass());
    }
}
