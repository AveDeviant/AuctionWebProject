package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.UserValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 12.04.2017.
 */
public class UserValidatorTest {

    @Test
    public void checkLoginPasswordTestInvalidInput() {
        String validUsername = "username";
        String invalidPassword = "password";
        boolean actual = UserValidator.checkLoginPassword(validUsername,invalidPassword);
        Assert.assertEquals(false,actual);
    }

    @Test
    public void checkLoginPasswordTestValidInput() {
        String validUsername="username";
        String validPassword = "passWor1d";
        boolean actual = UserValidator.checkLoginPassword(validUsername,validPassword);
        Assert.assertEquals(true,actual);
    }

    @Test
    public void checkEmailInvalidInput() {
        String invalidEmail = "bar.os@liscom";
        boolean actual = UserValidator.checkEmail(invalidEmail);
        Assert.assertEquals(false,actual);
    }

    @Test
    public void checkEmailValidInput() {
        String validEmail = "example@valid.org";
        boolean actual = UserValidator.checkEmail(validEmail);
        Assert.assertEquals(true,actual);
    }
}
