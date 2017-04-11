package by.buslauski.auction.action;

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
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    PageResponse execute(HttpServletRequest request);
}
