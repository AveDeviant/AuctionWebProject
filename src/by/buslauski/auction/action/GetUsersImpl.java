package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 28.03.2017.
 */
public class GetUsersImpl implements Command {
    private static final String CUSTOMERS = "customers";
    private static final String EMPTY_USER_LIST = "emptyList";
    private static UserService userService = new UserService();

    /**
     * Get user list from database
     *
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        try {
            ArrayList<User> customers = userService.getAllCustomers();
            request.setAttribute(CUSTOMERS, customers);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.USER_EDIT_PAGE);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
