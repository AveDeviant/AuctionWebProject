package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.PageBrowser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class BackCommandImpl implements Command {

    /**
     * Return to the previous page.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type: {@link ResponseType#REDIRECT}.
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
