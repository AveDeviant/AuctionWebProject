package by.buslauski.auction.dao;

import by.buslauski.auction.entity.AuctionStat;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.dao.exception.DAOException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface OrderDao {

    void addOrder(long customerId, long traderId, long lotId, BigDecimal price, boolean accept) throws DAOException;

    ArrayList<Order> getAllOrders() throws DAOException;

    ArrayList<Order> getUserConfirmedOrders(long userId) throws DAOException;

    AuctionStat calculateStatistic() throws DAOException;

    void deleteRejectedOrders(long userId) throws DAOException;

}
