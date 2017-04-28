package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface UserService {
    User authorizationChecking(String username, String password) throws ServiceException;

    void setWinner(User user) throws ServiceException;

    ArrayList<User> getAllCustomers() throws ServiceException;


    void updateUserInfo(long userId, String name, String city,
                        String address, String phone) throws ServiceException;

    void changeAccess(long userId, boolean access) throws ServiceException;

    User registerUser(String userName, String password, String email) throws ServiceException;

    User findAdmin() throws ServiceException;

    User findUserById(long userId) throws ServiceException;

    User findTrader(long lotId) throws ServiceException;

    ArrayList<Integer> defineRating();

    void updateTraderRating(long traderId, long customerId, int rating) throws ServiceException;

    void setTraderRating(User trader) throws ServiceException;
}
