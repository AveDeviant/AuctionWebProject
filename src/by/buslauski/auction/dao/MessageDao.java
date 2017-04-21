package by.buslauski.auction.dao;

import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.DAOException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface MessageDao {

    void addMessage(String theme, String text, long senderId, long recipientId) throws DAOException;

    ArrayList<UserMessage> findUserMessages(long userId) throws DAOException;

    long countUserUnreadMessages(long userId) throws DAOException;

    void changeMessageStatus(long userId) throws DAOException;
}
