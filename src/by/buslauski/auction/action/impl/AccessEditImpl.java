package by.buslauski.auction.action.impl;


import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.util.NumberParser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class AccessEditImpl implements Command {
    private static final String CUSTOMER_ID = "id";
    private static final String STATE = "state";
    private static final String UNBANNED = "unbanned";
    private static final String OPERATION_ERROR = "editErr";
    private static UserService userService = new UserServiceImpl();

    /**
     * Changing customer's access to bidding.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - {@link ResponseType#REDIRECT} in case operation passed successfully and
     * {@link ResponseType#FORWARD} in case operation failed (an exception has been thrown)
     * String page - page for response (current page)
     * @see Command#returnPageWithQuery(HttpServletRequest)
     * @see NumberParser
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(Command.returnPageWithQuery(request));
        long customerId = NumberParser.parse(request.getParameter(CUSTOMER_ID));
        String state = request.getParameter(STATE);
        boolean access = UNBANNED.equals(state);
        try {
            userService.changeAccess(customerId, access);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            request.setAttribute(OPERATION_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
