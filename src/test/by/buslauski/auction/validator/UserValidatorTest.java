package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.UserValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mikita Buslauski
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
        String invalidEmail1 = "bar.os@liscom";
        String invalidEmail2 = "testfailed.com";
        String invalidEmail3 = "<.olol@test.com";
        boolean[] expected = new boolean[]{false, false, false};
        boolean[] actual = new boolean[]{UserValidator.checkEmail(invalidEmail1),
                UserValidator.checkEmail(invalidEmail2),
                UserValidator.checkEmail(invalidEmail3)};

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void checkEmailValidInput() {
        String validEmail = "example@valid.org";
        boolean actual = UserValidator.checkEmail(validEmail);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void checkUserInfoInvalidInput() {
        String realName = "Incorrect 94 Name1";
        String city = "Berlin";
        String address ="tcc";
        String phone ="5455-21-52";
        Assert.assertFalse(UserValidator.checkUserInfo(realName,city,address,phone));
    }

    @Test
    public void checkUserInfoInvalidInputAnotherVariant() {
        String realName = "Johny Cash";
        String city = "Berlin";
        String address ="tcc";
        String phone ="525-2t-52";
        Assert.assertFalse(UserValidator.checkUserInfo(realName,city,address,phone));
    }
}
