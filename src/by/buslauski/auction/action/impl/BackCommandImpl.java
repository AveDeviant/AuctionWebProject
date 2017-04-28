package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.PageBrowser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 28.04.2017.
 */
public class BackCommandImpl implements Command {

    /**
     * Handling client request.
     *
     * @param request
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect).
     * String page - page for response.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        PageBrowser browser = (PageBrowser) request.getSession().getAttribute(SessionAttributes.PAGE_BROWSER);
        pageResponse.setResponseType(ResponseType.REDIRECT);
        String page = browser.getPreviousPage();
        System.out.println(page);
        pageResponse.setPage(page);
        return pageResponse;
    }
}
