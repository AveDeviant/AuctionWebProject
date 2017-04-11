package by.buslauski.auction.service;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.AbstractDao;
import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Acer on 21.03.2017.
 */
public class BetService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger();


    public void addBet(long userId, long lotId, BigDecimal price) throws ServiceException {
        ProxyConnection connection = null;
        try {
            connection = pool.takeConnectionFromPool();
            connection.setAutoCommit(false);
            BetDao betDao = new BetDao(connection);
            betDao.addBet(userId, lotId, price);
            LotDao lotDao = new LotDao(connection);
            lotDao.updateCurrentPrice(lotId, price);
            connection.commit();
        } catch (DAOException | SQLException e) {
            LOGGER.log(Level.ERROR, e + " Exception during adding bet in database");
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOGGER.log(Level.ERROR, e);
                }
            }
            throw new ServiceException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, e);
                }
            }
            returnConnection(connection);
        }
    }

    public User findUserByBet(Bet bet) throws ServiceException {
        User user = null;
        UserDao userDao = new UserDao();
        try {
            user = userDao.findUserById(bet.getUserId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;
    }


}
