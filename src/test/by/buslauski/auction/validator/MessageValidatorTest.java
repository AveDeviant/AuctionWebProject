package test.by.buslauski.auction.validator;

import by.buslauski.auction.validator.MessageValidator;
import org.junit.Assert;
import org.junit.Test;


public class MessageValidatorTest {

    @Test
    public void checkMessageInvalidTheme() {
        String theme = "THE length of this theme is greater than 45 symbols";
        String content = "Ticky-ticky Slim Shady";
        Assert.assertFalse(MessageValidator.checkMessage(theme, content));
    }

    @Test
    public void checkMessageCorrectThemeLength() {
        String theme = "Hi! My name is! What? My name is! WHO?";
        String content = "Ticky-ticky Slim Shady";
        Assert.assertTrue(MessageValidator.checkMessage(theme, content));
    }

    @Test
    public void checkMessageNullTheme() {
        String theme = null;
        String content = "I beginning to feel like a rap god, rap god";
        Assert.assertTrue(MessageValidator.checkMessage(theme, content));
    }
}
