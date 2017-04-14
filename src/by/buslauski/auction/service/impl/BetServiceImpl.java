package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.UserDao;
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

/**
 * Created by Acer on 21.03.2017.
 */
public class BetServiceImpl extends AbstractService implements BetService {
    private static final Logger LOGGER = LogManager.getLogger();


    @Override
    public void addBet(long userId, long lotId, BigDecimal price) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            BetDao betDao = new BetDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            daoHelper.beginTransaction(betDao, lotDao);
            betDao.addBet(userId, lotId, price);
            lotDao.updateCurrentPrice(lotId, price);
            daoHelper.commit();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during adding bet in database");
            daoHelper.rollback();
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();

        }

    }

    @Override
    public User findUserByBet(Bet bet) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            user = userDao.findUserById(bet.getUserId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
    }

    @Override
    public boolean checkBetValue(Lot lot, BigDecimal bet) {
        BigDecimal price = lot.getCurrentPrice();
        return (bet.compareTo(price) > 0);
    }


}
