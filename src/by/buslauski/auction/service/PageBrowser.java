package by.buslauski.auction.service;

import by.buslauski.auction.constant.PageNavigation;

import java.util.ArrayDeque;

/**
 * @author Buslauski Mikita
 */
public class PageBrowser {
    private ArrayDeque<String> history;

    public PageBrowser() {
        history = new ArrayDeque<>();
    }

    public void addPageToHistory(String page) {
        history.addLast(page);
    }

    public String getPreviousPage() {
        return history.isEmpty() ? PageNavigation.INDEX_PAGE : history.pollLast();

    }
}
