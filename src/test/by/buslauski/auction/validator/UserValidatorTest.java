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
        String validLogin = "username";
        String invalidPassword = "password";
        String alias = "My alias is pretty good";
        boolean actual = UserValidator.checkLoginPasswordAlias(validLogin, invalidPassword, alias);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void checkLoginPasswordTestValidInput() {
        String validUsername = "username";
        String validPassword = "passWor1d";
        String alias = "My alias is pretty good";
        boolean actual = UserValidator.checkLoginPasswordAlias(validUsername, validPassword, alias);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void checkEmailInvalidInput() {
        String invalidEmail = "bar.os@liscom";
        boolean actual = UserValidator.checkEmail(invalidEmail);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void checkEmailValidInput() {
        String validEmail = "example@valid.org";
        boolean actual = UserValidator.checkEmail(validEmail);
        Assert.assertEquals(true, actual);
    }
}
