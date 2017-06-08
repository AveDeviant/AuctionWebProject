package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class LogoutCommandImpl implements Command {

    /**
     * Invalidates current session and redirects client to the index page.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - {@link ResponseType#REDIRECT};
     * String page - page for response {@link PageNavigation#INDEX_PAGE}.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        HttpSession session = request.getSession();
        session.invalidate();
        pageResponse.setResponseType(ResponseType.REDIRECT);
        pageResponse.setPage(PageNavigation.INDEX_PAGE);
        return pageResponse;
    }
}
