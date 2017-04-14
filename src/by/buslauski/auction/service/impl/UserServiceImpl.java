package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.impl.BetDaoImpl;
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
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            String passwordMD5 = Md5Converter.convertToMd5(password);
            String returnedPwd = userDao.checkPasswordByLogin(username);
            if (PASSWORD_NOT_FOUND.equals(returnedPwd)) {
                return user;
            }
            if (passwordMD5.equals(returnedPwd)) {
                user = userDao.findUserByUsername(username);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;
    }

    /**
     * Check lots which unable for bids but already have confirmed bets.
     * Last bet is a winning bet - the last consumer who made a bet is a winner of auction.
     *
     * @param user add winning bets in consumer's ArrayList for further order.
     */
    @Override
    public void setWinner(User user) throws ServiceException {
        ArrayList<Lot> lots = lotService.getLotsWithOverTiming();
        ArrayList<Bet> winningBets = new ArrayList<>();

        for (Lot lot : lots) {
            if (!lot.getBets().isEmpty() && lot.getAvailability()) {   // if lot have confirmed bets
                Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
                if (lastBet.getUserId() == user.getUserId()) {
                    if (lotService.checkWaitingPeriod(lot)) {
                        winningBets.add(lastBet);
                    } else {                            // if the winnings are not processed within 10 days.
                        lotService.resetBids(lot);
                    }
                }
            }
            user.setWinningBets(winningBets);
        }
    }

    @Override
    public ArrayList<User> getAllCustomers() throws ServiceException {
        ArrayList<User> users = null;
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            users = userDao.getAllCustomers();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return users;
    }

    @Override
    public ArrayList<Bet> getUserBets(User user) throws ServiceException {
        ArrayList<Bet> bets = null;
        BetDaoImpl betDao = new BetDaoImpl();
        try {
            bets = betDao.getUserBets(user.getUserId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            betDao.returnConnection();
        }
        return bets;
    }

    @Override
    public void updateUserInfo(long userId, String name, String city,
                               String address, String phone) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            userDao.updateUserInfo(userId, name, city, address, phone);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
    }

    /**
     * Ban or unban costumer using customer ID.
     *
     * @param userId  User ID whose status should be updated.
     * @param access true - unban costumer
     *               false - ban costumer
     */
    @Override
    public void changeAccess(long userId, boolean access) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            userDao.changeAccess(userId, access);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
    }

    @Override
    public User registerUser(String userName, String password, String email) throws ServiceException {
        User user = null;
        UserDaoImpl userDao = new UserDaoImpl();
        if (!UserValidator.checkEmail(email)) {
            return user;
        }
        try {
            if (uniqueCheck(userName, email)) {
                String pwdMd5 = Md5Converter.convertToMd5(password);
                userDao.addUser(ROLE_ID_CUSTOMER, userName, email, pwdMd5);
                user = userDao.findUserByUsername(userName);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;

    }

    @Override
    public User findAdmin() throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        User user = null;
        try {
            user = userDao.findAdmin();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;
    }

    @Override
    public User findUserById(long userId) throws ServiceException {
        User user = null;
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            user = userDao.findUserById(userId);
        } catch (DAOException e) {

            throw new ServiceException(e);
        } finally {
            userDao.returnConnection();
        }
        return user;
    }

    /** Checking user's username and password for uniqueness
     * @param userName entered username
     * @param email    entered password
     * @return false - entered values have been already used by other users.
     * true - database doesn't contains entered values.
     */
    private boolean uniqueCheck(String userName, String email) throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            if (!userDao.checkPasswordByLogin(userName).equals("") || userDao.findUserByEmail(email)) {
                return false;
            }
        } catch (DAOException e) {
            throw new ServiceException();
        }
        return true;
    }
}