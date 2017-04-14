package by.buslauski.auction.dao;

import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.DAOException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface MessageDao {

    void addMessage(String theme, String text, User sender, User recipient) throws DAOException;

    ArrayList<UserMessage> findUserMessages(long userId) throws DAOException;
}
