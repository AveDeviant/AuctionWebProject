package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.AuctionStat;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.OrderService;
import by.buslauski.auction.service.impl.OrderServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class OrderServiceTest {
    private static OrderService orderService;

    @BeforeClass
    public static void init() {
        orderService = new OrderServiceImpl();
    }

    /**
     * Assume that database stores confirmed orders (deals)
     *
     * @throws ServiceException if DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void getOrdersTest() throws ServiceException {
        ArrayList<Order> orders = orderService.getAllOrders();
        Assert.assertTrue(orders.size() > 0);
    }

    /**
     * Note that database already stores order where one of the contract side
     * was user with ID=1.
     *
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Test
    public void getUserConfirmedOrders() throws ServiceException {
        ArrayList<Order> orders = orderService.getUserConfirmedOrders(1);
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void getAuctionStatTest() throws ServiceException {
        AuctionStat auctionStat = orderService.calculateStatistic();
        Assert.assertTrue(new BigDecimal(0).compareTo(auctionStat.getDealsSum()) < 0);
    }

    @Test
    public void getAuctionStatTest2() throws ServiceException {
        AuctionStat auctionStat = orderService.calculateStatistic();
        Assert.assertTrue(0 < auctionStat.getDealsCount());
    }
}
