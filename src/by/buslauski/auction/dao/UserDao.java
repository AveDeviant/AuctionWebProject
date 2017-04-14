package by.buslauski.auction.dao;

import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface UserDao {

    User findUserById(long userId) throws DAOException;

    void addUser(int id_role, String userName, String email, String password) throws DAOException;

    ArrayList<User> getAllCustomers() throws DAOException;

    String findPasswordByLogin(String username) throws DAOException;

    User findUserByUsername(String username) throws DAOException;

    void changeAccess(long userId, boolean access) throws DAOException;

    boolean findUserByEmail(String email) throws DAOException;

    void updateUserInfo(long userId, String realName, String city,
                        String address, String phone) throws DAOException;

    User findAdmin() throws DAOException;
}
