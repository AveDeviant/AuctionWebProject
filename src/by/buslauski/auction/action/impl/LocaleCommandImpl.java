package by.buslauski.auction.action.impl;


import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class LocaleCommandImpl implements Command {
    private static final String JSP_PATH = "jspPath";
    private static final String LOCAL_ATTRIBUTE = "locale";
    private static final String SELECTED_LANGUAGE = "lang";

    /**
     * Change application localization.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - response type:  {@link ResponseType#FORWARD}
     * String page - page for response (current page).
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String locale = request.getParameter(SELECTED_LANGUAGE);
        HttpSession session = request.getSession();
        pageResponse.setResponseType(ResponseType.FORWARD);
        session.setAttribute(LOCAL_ATTRIBUTE, locale);
        String path = request.getParameter(JSP_PATH);
        String main = PageNavigation.MAIN_PAGE + "?";
        if (main.equals(path)) {
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        } else {
            pageResponse.setPage(returnPageWithQuery(request));
        }
        return pageResponse;
    }
}
