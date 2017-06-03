package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface AuctionService {
    int REJECTED_DEALS_COUNT = 5; // if customer exceed this number system block him.
    int WAITING_PERIOD = 10;  // waiting period for registration of the won lot, in days.
    int EXTENDING_PERIOD_MIN = 7; // extended bidding period,bottom line, in days.
    int EXTENDING_PERIOD_MAX = 15; // extended bidding period, upper bound, in days.
    int AUCTION_STEP_PERCENT = 5;  // auction step, in percents.
    int AUCTION_BIDDING_PERIOD_MAX = 30; // auction bidding period, in days.

    void setWinner(User user) throws ServiceException;

    void setWinner() throws ServiceException;

    boolean notifyTrader(Bet bet) throws ServiceException;

    void resetBids(Lot lot) throws ServiceException;
}
