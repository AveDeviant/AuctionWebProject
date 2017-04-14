package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.OrderService;
import by.buslauski.auction.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 30.03.2017.
 */
public class GetOrdersCommandImpl implements Command {
    private static final String ORDERS = "orders";
    private static final String ERROR = "err";
    private static OrderService orderService = new OrderServiceImpl();

    /**
     * Get order list from database.
     *
     * @param request
     * @return PageResponse object containing two fields:
     * ResponseType - response type (forward or redirect)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
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
