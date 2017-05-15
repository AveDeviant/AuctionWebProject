package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface AuctionService {

    void setWinner(User user) throws ServiceException;

    boolean notifyTrader(Bet bet) throws ServiceException;
}
