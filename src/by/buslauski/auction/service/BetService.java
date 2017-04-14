package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BetService {
    void addBet(long userId, long lotId, BigDecimal price) throws ServiceException;

    User findUserByBet(Bet bet) throws ServiceException;

    boolean checkBetValue(Lot lot, BigDecimal bet);
}
