package by.buslauski.auction.action;

import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.response.PageResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 04.03.2017.
 */
public interface Command {
    Logger LOGGER = LogManager.getLogger();

    /**
     * @param request
     * @return An object containing two fields:
     * PageResponse - response type (forward or redirect)
     * String page - page for response
     */
    PageResponse execute(HttpServletRequest request);

    default String returnPageWithQuery(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            return path;
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            return controller + query;
        }
    }
}
