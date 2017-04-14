package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 28.03.2017.
 */
public class GetUsersImpl implements Command {
    private static final String CUSTOMERS = "customers";
    private static final String EMPTY_USER_LIST = "emptyList";
    private static UserService userService = new UserServiceImpl();

    /**
     * Get user list from database
     *
     * @param request
     * @return An object PageResponse containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
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
