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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Acer on 21.03.2017.
 */
public class BetServiceImpl extends AbstractService implements BetService {
    private static ReentrantLock lock = new ReentrantLock();


    @Override
    public boolean addBet(long userId, long lotId, BigDecimal price) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        boolean flag = false;
        try {
            lock.lock();
            BetDao betDao = new BetDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            daoHelper.beginTransaction(betDao, lotDao);
            Lot lot = lotDao.findLotById(lotId);
            if (price.compareTo(lot.getCurrentPrice()) <= 0) {
                return flag;
            }
            betDao.addBet(userId, lotId, price);
            lotDao.updateCurrentPrice(lotId, price);
            daoHelper.commit();
            flag = true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during adding bet in database");
            daoHelper.rollback();
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
            lock.lock();
        }
        return flag;
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
        return bet.compareTo(price) > 0;
    }

    @Override
    public ArrayList<Bet> getUserBets(User user) throws ServiceException {
        ArrayList<Bet> bets = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            BetDao betDao = new BetDaoImpl();
            daoHelper.initDao(betDao);
            bets.addAll(betDao.getUserBets(user.getUserId()));
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return bets;
    }


}
