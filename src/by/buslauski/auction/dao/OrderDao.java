package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.DAOException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface OrderDao {

    void addOrder(long customerId, long lotId, BigDecimal price) throws DAOException;

    ArrayList<Order> getAllOrders() throws DAOException;
}
