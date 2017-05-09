package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.util.Md5Converter;
import by.buslauski.auction.validator.UserValidator;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 24.03.2017.
 */
public class UserServiceImpl extends AbstractService implements UserService {
    private static final String PASSWORD_NOT_FOUND = "";
    private static final int ROLE_ID_CUSTOMER = 2;
    private static LotService lotService = new LotServiceImpl();


    @Override
    public User authorizationChecking(String username, String password) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            String passwordMD5 = Md5Converter.convertToMd5(password);
            String returnedPwd = userDao.findPasswordByLogin(username);
            if (PASSWORD_NOT_FOUND.equals(returnedPwd)) {
                return user;
            }
            if (passwordMD5.equals(returnedPwd)) {
                user = userDao.findUserByUsername(username);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
    }

    /**
     * Check lots which unable for bids but already have confirmed bets.
     * Last bet is a winning bet - the last consumer who made a bet is a winner of auction.
     * Add winning bets in consumer's ArrayList for further ordering.
     *
     * @param user auction customer.
     */
    @Override
    public void setWinner(User user) throws ServiceException {
        ArrayList<Lot> lots = lotService.getLotsWithOverTiming();
        ArrayList<Bet> winningBets = new ArrayList<>();
        for (Lot lot : lots) {
            if (!lot.getBets().isEmpty() && lot.getAvailability()) {   // if lot has confirmed bets
                Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
                if (lastBet.getUserId() == user.getUserId()) {
                    winningBets.add(lastBet);
                }
            }
            user.setWinningBets(winningBets);
        }
    }

    @Override
    public ArrayList<User> getAllCustomers() throws ServiceException {
        ArrayList<User> users = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            users.addAll(userDao.getAllCustomers());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return users;
    }

    @Override
    public void updateUserInfo(long userId, String name, String city,
                               String address, String phone) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            userDao.updateUserInfo(userId, name, city, address, phone);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Ban or unban costumer using customer ID.
     *
     * @param userId User ID whose status should be updated.
     * @param access true - unban costumer
     *               false - ban costumer
     */
    @Override
    public void changeAccess(long userId, boolean access) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            userDao.changeAccess(userId, access);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    @Override
    public User registerUser(String userName, String password, String alias, String email) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        if (!UserValidator.checkEmail(email)) {
            return user;
        }
        try {
            if (uniqueCheck(userName, email, alias)) {
                UserDao userDao = new UserDaoImpl();
                daoHelper.initDao(userDao);
                String pwdMd5 = Md5Converter.convertToMd5(password);
                userDao.addUser(ROLE_ID_CUSTOMER, userName, email, pwdMd5, alias);
                user = userDao.findUserByUsername(userName);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;

    }

    @Override
    public User findAdmin() throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            user = userDao.findAdmin();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
    }

    @Override
    public User findUserById(long userId) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            user = userDao.findUserById(userId);
        } catch (DAOException e) {

            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
    }

    @Override
    public User findTrader(long lotId) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            user = userDao.findTrader(lotId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
    }

    @Override
    public ArrayList<Integer> defineRating() {
        ArrayList<Integer> rating = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            rating.add(i);
        }
        return rating;
    }

    @Override
    public void updateTraderRating(long traderId, long customerId, int rating) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            userDao.updateTraderRating(traderId, customerId, rating);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void setTraderRating(User trader) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            double rating = userDao.findTraderRating(trader.getUserId());
            BigDecimal bigDecimal = new BigDecimal(rating);
            rating = bigDecimal.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();  //rating rounding.
            trader.setUserRating(rating);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Checking user's login, email and alias for uniqueness.
     *
     * @param login entered login
     * @param email entered email
     * @param alias entered alias (username)
     * @return false - entered values have been already used by other users.
     * true - database doesn't contains entered values.
     */
    private boolean uniqueCheck(String login, String email, String alias) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            if (!userDao.findPasswordByLogin(login).equals("") || userDao.findUserByEmailAlias(email, alias)) {
                return false;
            }
        } catch (DAOException e) {
            throw new ServiceException();
        } finally {
            daoHelper.release();
        }
        return true;
    }
}
