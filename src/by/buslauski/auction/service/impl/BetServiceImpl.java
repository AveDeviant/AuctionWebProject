package by.buslauski.auction.service.impl;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.impl.BetDaoImpl;
import by.buslauski.auction.dao.impl.LotDaoImpl;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.BetService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Acer on 21.03.2017.
 */
public class BetServiceImpl extends AbstractService implements BetService {
    private static final Logger LOGGER = LogManager.getLogger();


    @Override
    public void addBet(long userId, long lotId, BigDecimal price) throws ServiceException {
        ProxyConnection connection = null;
        try {
            connection = pool.takeConnectionFromPool();
            connection.setAutoCommit(false);
            BetDaoImpl betDao = new BetDaoImpl(connection);
            betDao.addBet(userId, lotId, price);
            LotDaoImpl lotDao = new LotDaoImpl(connection);
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

    @Override
    public User findUserByBet(Bet bet) throws ServiceException {
        User user = null;
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            user = userDao.findUserById(bet.getUserId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;
    }

    @Override
    public boolean checkBetValue(Lot lot, BigDecimal bet) {
        BigDecimal price = lot.getCurrentPrice();
        return (bet.compareTo(price) > 0);
    }


}
