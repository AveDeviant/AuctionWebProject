package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public interface Command {
    Logger LOGGER = LogManager.getLogger();

    /**
     * Handling client request.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type (forward or redirect).
     * String page - page for response.
     */
    PageResponse execute(HttpServletRequest request);

    /**
     * Get query string from URI.
     *
     * @param request client request to get parameters to work with.
     * @return current page.
     */
    static String returnPageWithQuery(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String path = request.getParameter(SessionAttributes.JSP_PATH);
        if (path == null || path.trim().isEmpty()){
            return PageNavigation.INDEX_PAGE;
        }
            if (path.endsWith("?")) {
                return path;
            } else {
                String query = path.substring(path.lastIndexOf("?"));
                return controller + query;
            }
    }

    static String definePathToSuccessPage(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String command = "command=goTo";
        String successPage = "page=success";
        StringBuilder stringBuilder = new StringBuilder(controller);
        stringBuilder.append("?").append(command);
        stringBuilder.append("&").append(successPage);
        return stringBuilder.toString();
    }

    static String definePathToAccessDeniedPage(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String command = "command=goTo";
        String successPage = "page=accessDenied";
        StringBuilder stringBuilder = new StringBuilder(controller);
        stringBuilder.append("?").append(command);
        stringBuilder.append("&").append(successPage);
        return stringBuilder.toString();
    }

}
