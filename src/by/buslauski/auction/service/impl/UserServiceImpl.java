package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.util.Md5Converter;
import by.buslauski.auction.validator.UserValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class UserServiceImpl extends AbstractService implements UserService {
    private static final String PASSWORD_NOT_FOUND = "";
    private static final int ROLE_ID_CUSTOMER = 2;

    @Override
    public User authorizationChecking(String login, String password) throws ServiceException {
        User user = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            String passwordMD5 = Md5Converter.convertToMd5(password);
            String returnedPwd = userDao.findPasswordByLogin(login);
            if (PASSWORD_NOT_FOUND.equals(returnedPwd)) {
                return user;
            }
            if (passwordMD5.equals(returnedPwd)) {
                user = userDao.findUserByLogin(login);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;
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
            e.printStackTrace();
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return users;
    }

    /**
     * Insert user personal information into database.
     *
     * @param userId  user ID.
     * @param name    user's real name.
     * @param city    user's city.
     * @param address user's address.
     * @param phone   user's contact phone.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     * @see by.buslauski.auction.action.impl.customer.BuyLotImpl#execute(HttpServletRequest)
     */
    @Override
    public void updateUserInfo(long userId, String name, String city,
                               String address, String phone) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            userDao.updateUserInfo(userId, name, city, address, phone);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Ban or unban customer using customer ID.
     *
     * @param userId User ID whose access should be updated.
     * @param access true - unban costumer.
     *               false - ban costumer.
     * @see by.buslauski.auction.action.impl.AccessEditImpl#execute(HttpServletRequest)
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

    /**
     * Creating a new account using entered values by new user.
     *
     * @param userName user login;
     * @param password user password;
     * @param alias    user alias;
     * @param email    user e-mail;
     * @return new defined {@link User}
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     * @see UserServiceImpl#uniqueCheck(String, String, String)
     * @see by.buslauski.auction.action.impl.RegistrationCommandImpl#execute(HttpServletRequest)
     */
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
                user = userDao.findUserByLogin(userName);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return user;

    }

    /**
     * This method find {@link User} with {@link by.buslauski.auction.entity.Role#ADMIN}
     *
     * @return defined {@link User}.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     */
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

    /**
     * Initializing <code>ArrayList</code> containing five numeric values (from 1 to 5) which are used as trader's rating
     * upon completion of the deal.
     *
     * @return {@link ArrayList} object.
     */
    @Override
    public ArrayList<Integer> defineRating() {
        ArrayList<Integer> rating = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            rating.add(i);
        }
        return rating;
    }

    /**
     * Insert a new trader rating value into database.
     *
     * @param traderId   ID of trader whose rating should be updated.
     * @param customerId ID of customer who update trader rating.
     * @param rating     rating value.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     * @see by.buslauski.auction.action.impl.customer.TraderRatingImpl#execute(HttpServletRequest)
     */
    @Override
    public void updateTraderRating(long traderId, long customerId, int rating) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        if (rating < 1 || rating > 5 || traderId == customerId) {
            return;
        }
        try {
            UserDao userDao = new UserDaoImpl();
            daoHelper.initDao(userDao);
            userDao.updateTraderRating(traderId, customerId, rating);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        }
    }

    /**
     * Calculate trader rating.
     * Get average value fom database and round it using <code>BigDecimal</code>
     * Set calculated rating to trader. {@link User#userRating}
     *
     * @param trader trader whose rating should be calculated.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     */
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

    @Override
    public void blockUser(int rejectedDeals) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        UserDao userDao = new UserDaoImpl();
        try {
            daoHelper.initDao(userDao);
            userDao.blockUser(rejectedDeals);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Checking customer's login, email and alias for uniqueness.
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
