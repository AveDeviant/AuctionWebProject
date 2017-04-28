package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 14.03.2017.
 */
public class LogoutCommandImpl implements Command {

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
