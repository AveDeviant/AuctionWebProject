package by.buslauski.auction.action;


import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 28.03.2017.
 */
public class AccessEditImpl implements Command {
    private static final String CUSTOMER_ID = "id";
    private static final String STATE = "state";
    private static final String UNBANNED = "unbanned";
    private static final String OPERATION_ERROR = "editErr";
    private static UserService userService = new UserServiceImpl();

    /**
     * Changing customer's access to bidding
     *
     * @param request
     * @return PageResponse - An object containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
        long customerId = Long.parseLong(request.getParameter(CUSTOMER_ID));
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
