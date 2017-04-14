package by.buslauski.auction.dao;

import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;

/**
 * Created by Acer on 14.04.2017.
 */
public interface UserDao {

    User findUserById(long userId) throws DAOException;

    void addUser(int id_role, String userName, String email, String password) throws DAOException;
}
