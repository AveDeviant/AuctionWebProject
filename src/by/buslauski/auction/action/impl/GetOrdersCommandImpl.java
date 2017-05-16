package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.OrderService;
import by.buslauski.auction.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class GetOrdersCommandImpl implements Command {
    private static final String ORDERS = "orders";
    private static final String ERROR = "err";
    private static OrderService orderService = new OrderServiceImpl();

    /**
     * Get orders (deals) from database.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type: {@link ResponseType#FORWARD}
     * String page - page for response "/jsp/orders.jsp"
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (currentUser == null || Role.ADMIN != currentUser.getRole()) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
            return pageResponse;
        }
        try {
            ArrayList<Order> orders = orderService.getAllOrders();
            request.setAttribute(ORDERS, orders);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.ORDERS_SHOW_PAGE);
        } catch (ServiceException e) {
            request.setAttribute(ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.ORDERS_SHOW_PAGE);
        }
        return pageResponse;
    }
}
