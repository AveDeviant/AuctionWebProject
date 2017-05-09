package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.OrderService;
import by.buslauski.auction.service.impl.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Acer on 05.05.2017.
 */
public class OrderServiceTest {

    /**
     * Assume that database stores confirmed orders (deals)
     *
     * @throws ServiceException if DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void getOrdersTest() throws ServiceException {
        OrderService orderService = new OrderServiceImpl();
        ArrayList<Order> orders = orderService.getAllOrders();
        Assert.assertTrue(orders.size() > 0);
    }
}
