package by.buslauski.auction.action;

import by.buslauski.auction.constant.SessionAttributes;
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
     * Handling client request.
     *
     * @param request
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect).
     * String page - page for response.
     */
    PageResponse execute(HttpServletRequest request);

    /**
     * Get query string from URI.
     *
     * @param request
     * @return
     */
    default String returnPageWithQuery(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String path = request.getParameter(SessionAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            return path;
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            return controller + query;
        }
    }

    default String definePathToSuccessPage(HttpServletRequest request) {
        String controller = request.getRequestURI();
        String command = "command=goTo";
        String successPage = "page=success";
        StringBuilder stringBuilder = new StringBuilder(controller);
        stringBuilder.append("?").append(command);
        stringBuilder.append("&").append(successPage);
        return stringBuilder.toString();
    }

}
