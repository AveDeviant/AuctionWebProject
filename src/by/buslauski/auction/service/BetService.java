package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BetService {
    boolean addBet(long userId, long lotId, BigDecimal price) throws ServiceException;

    User findUserByBet(Bet bet) throws ServiceException;

    boolean checkBetValue(Lot lot, BigDecimal bet);

    ArrayList<Bet> getUserBets(User user) throws ServiceException;
}
