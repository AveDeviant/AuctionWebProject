package by.buslauski.auction.dao;

import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.dao.exception.DAOException;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface MessageDao {

    void addMessage(String theme, String text, long senderId, long recipientId) throws DAOException;

    ArrayList<UserMessage> findUserMessages(long userId) throws DAOException;

    long countUserUnreadMessages(long userId) throws DAOException;

    void changeMessageStatus(long userId) throws DAOException;

}
