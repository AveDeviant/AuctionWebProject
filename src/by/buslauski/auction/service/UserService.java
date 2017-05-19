package by.buslauski.auction.service;

import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;

import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface UserService {

    User authorizationChecking(String username, String password) throws ServiceException;

    ArrayList<User> getAllCustomers() throws ServiceException;


    void updateUserInfo(long userId, String name, String city,
                        String address, String phone) throws ServiceException;

    void changeAccess(long userId, boolean access) throws ServiceException;

    User registerUser(String userName, String password, String alias, String email) throws ServiceException;

    User findAdmin() throws ServiceException;

    User findUserById(long userId) throws ServiceException;

    ArrayList<Integer> defineRating();

    void updateTraderRating(long traderId, long customerId, int rating) throws ServiceException;

    void setTraderRating(User trader) throws ServiceException;

    void blockUser(int rejectedDeals) throws ServiceException;
}
