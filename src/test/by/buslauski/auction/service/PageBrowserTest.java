package test.by.buslauski.auction.service;

import by.buslauski.auction.service.PageBrowser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Acer on 21.05.2017.
 */
public class PageBrowserTest {


    @Test
    public void getPageTestEmptyQueue(){
        PageBrowser pageBrowser = new PageBrowser();
        Assert.assertEquals("/index.jsp",pageBrowser.getPreviousPage());
    }
}
