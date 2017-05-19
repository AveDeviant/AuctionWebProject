package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.dao.exception.DAOException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface BetDao {

    void addBet(long userId, long lotId, BigDecimal price) throws DAOException;

    ArrayList<Bet> getBetsByLotId(long lotId) throws DAOException;

    ArrayList<Bet> getUserBets(long userId) throws DAOException;

    void resetBets(long lotId) throws DAOException;

    int countLotFollowers(long lotId) throws DAOException;
}
