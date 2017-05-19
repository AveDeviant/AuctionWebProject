package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface AuctionService {
    int REJECTED_DEALS_COUNT = 5; // if customer exceed this number system block him.

    void setWinner(User user) throws ServiceException;

    void setWinner() throws ServiceException;

    boolean notifyTrader(Bet bet) throws ServiceException;
}
