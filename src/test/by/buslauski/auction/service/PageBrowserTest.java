package test.by.buslauski.auction.service;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.service.PageBrowser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mikita Buslauski
 */
public class PageBrowserTest {

    @Test
    public void getPageTestEmptyQueue(){
        PageBrowser pageBrowser = new PageBrowser();
        Assert.assertEquals(PageNavigation.INDEX_PAGE ,pageBrowser.getPreviousPage());
    }

    @Test
    public void getPageTestQueueContainsPage() {
        PageBrowser pageBrowser = new PageBrowser();
        pageBrowser.addPageToHistory(PageNavigation.AUTHORIZATION_PAGE);
        Assert.assertEquals(PageNavigation.AUTHORIZATION_PAGE, pageBrowser.getPreviousPage());
    }
}
