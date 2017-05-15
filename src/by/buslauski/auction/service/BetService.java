package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface BetService {
    int AUCTION_STEP_PERCENT = 5;

    boolean addBet(long userId, long lotId, BigDecimal price) throws ServiceException;

    boolean checkBetValue(Lot lot, BigDecimal bet);

    ArrayList<Bet> getUserBets(User user) throws ServiceException;
}
