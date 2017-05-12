package by.buslauski.auction.service;

import java.util.ArrayDeque;

/**
 * Created by Acer on 27.04.2017.
 */
public class PageBrowser {
    private ArrayDeque<String> history = new ArrayDeque<>();

    public void addPageToHistory(String page) {
        history.addLast(page);
    }

    public String getPreviousPage() {
        return history.pollLast();

    }
}
