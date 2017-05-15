package by.buslauski.auction.service;

import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface OrderService {
    ArrayList<Order> getAllOrders() throws ServiceException;

    ArrayList<Order> getUserConfirmedOrders(long userId) throws ServiceException;

}
