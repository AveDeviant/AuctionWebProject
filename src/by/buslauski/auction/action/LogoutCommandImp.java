package by.buslauski.auction.action;

import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 14.03.2017.
 */
public class LogoutCommandImp implements Command {

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        HttpSession session = request.getSession();
        session.setAttribute(RequestAttributes.USER, null);
        pageResponse.setResponseType(ResponseType.REDIRECT);
        pageResponse.setPage(PageNavigation.INDEX_PAGE);
        return pageResponse;
    }
}
