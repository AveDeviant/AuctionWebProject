package test.by.buslauski.auction.action;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.RegistrationCommandImpl;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.ResponseType;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 16.05.2017.
 */
public class RegistrationCommandTest {

    @Test
    public void executeNotEqualPasswords() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Command registerUser = new RegistrationCommandImpl();
        when(request.getParameter("password")).thenReturn("SlimShady");
        when(request.getParameter("password2")).thenReturn("MarshallMathers");
        Assert.assertTrue(registerUser.execute(request).getResponseType() == ResponseType.FORWARD);
    }

    @Test
    public void executeEmailAlreadyExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Command registerUser = new RegistrationCommandImpl();
        when(request.getParameter("email")).thenReturn("buslauskima@gmail.com");
        Assert.assertTrue(registerUser.execute(request).getResponseType() == ResponseType.FORWARD);

    }
}
