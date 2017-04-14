package by.buslauski.auction.action.impl;


import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 22.02.2017.
 */
public class LocaleCommandImpl implements Command {
    private static final String JSP_PATH = "jspPath";
    private static final String LOCAL_ATTRIBUTE = "locale";
    private static final String SELECTED_LANGUAGE = "lang";

    /** Change application localization.
     *
     * @param request
     * @return
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String locale = request.getParameter(SELECTED_LANGUAGE);
        HttpSession session = request.getSession();
        String controller = request.getRequestURI();
        pageResponse.setResponseType(ResponseType.REDIRECT);
        session.setAttribute(LOCAL_ATTRIBUTE, locale);
        String path = request.getParameter(JSP_PATH);
        String main = PageNavigation.MAIN_PAGE + "?";
        if (main.equals(path)) {
            pageResponse.setPage(controller);
            return pageResponse;
        }
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
        return pageResponse;
    }
}
