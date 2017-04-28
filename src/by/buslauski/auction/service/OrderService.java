package by.buslauski.auction.service;

import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface OrderService {
    ArrayList<Order> getAllOrders() throws ServiceException;

    ArrayList<Order> getUserConfirmedOrders(long userId) throws ServiceException;

}
