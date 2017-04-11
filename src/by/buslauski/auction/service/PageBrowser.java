package by.buslauski.auction.service;

import java.util.ArrayDeque;

/**
 * Created by Acer on 24.02.2017.
 */
public class PageBrowser {
private  ArrayDeque<String> pageHistory = new ArrayDeque<>();

public  void addPageToHistory(String path) {
    pageHistory.addLast(path);
}

public  String getPreviousPage() {
    return pageHistory.pollLast();
}

}
