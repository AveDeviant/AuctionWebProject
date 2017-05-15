package test.by.buslauski.auction.util;

import by.buslauski.auction.util.Md5Converter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 01.05.2017.
 */
public class MD5ConverterTest {

    @Test
    public void convertToMd5Test(){
        String password = "EnteredPassword95";
        String hash = "7475ec39fc03a1a3288d68a022b3ff9a";
        String actual = Md5Converter.convertToMd5(password);
        Assert.assertEquals(hash,actual);
    }

}
