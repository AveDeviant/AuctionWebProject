package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.exception.DAOException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BetDao {

    void addBet(long userId, long lotId, BigDecimal price) throws DAOException;

    ArrayList<Bet> getBetsByLotId(long lotId) throws DAOException;
}
