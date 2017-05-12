package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

/**
 * Created by Acer on 10.05.2017.
 */
public interface AuctionService {

    void setWinner(User user) throws ServiceException;

    boolean notifyTrader(Bet bet) throws ServiceException;
}
