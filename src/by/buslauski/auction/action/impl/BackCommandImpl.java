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
     * Return to previous page.
     *
     * @param request user's request.
     * @return <code>PageResponse</code> object containing two fields:
     * ResponseType - REDIRECT.
     * String page - page for response.
     * @see PageBrowser
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        PageBrowser browser = (PageBrowser) request.getSession().getAttribute(SessionAttributes.PAGE_BROWSER);
        pageResponse.setResponseType(ResponseType.REDIRECT);
        String page = browser.getPreviousPage();
        pageResponse.setPage(page);
        return pageResponse;
    }
}
