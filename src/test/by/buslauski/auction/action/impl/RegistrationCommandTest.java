package test.by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.impl.RegistrationCommandImpl;
import by.buslauski.auction.response.ResponseType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 16.05.2017.
 */
public class RegistrationCommandTest {
    private static HttpServletRequest request;
    private static Command registerUser;

    @BeforeClass
    public static void init() {
        request = Mockito.mock(HttpServletRequest.class);
        registerUser = new RegistrationCommandImpl();
    }

    @Test
    public void executeNotEqualPasswords() {
        when(request.getParameter("password")).thenReturn("SlimShady");
        when(request.getParameter("password2")).thenReturn("MarshallMathers");
        Assert.assertTrue(registerUser.execute(request).getResponseType() == ResponseType.FORWARD);
    }

    @Test
    public void executeEmailAlreadyExists() {
        when(request.getParameter("email")).thenReturn("buslauskima@gmail.com");
        Assert.assertTrue(registerUser.execute(request).getResponseType() == ResponseType.FORWARD);

    }
}
