package by.buslauski.auction.service;

import by.buslauski.auction.entity.AuctionStat;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.service.exception.ServiceException;

import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface OrderService {

    ArrayList<Order> getAllOrders() throws ServiceException;

    ArrayList<Order> getUserConfirmedOrders(long userId) throws ServiceException;

    AuctionStat calculateStatistic() throws ServiceException;

}
