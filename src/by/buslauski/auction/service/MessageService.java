package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.ServiceException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface MessageService {
    void addMessage(String theme, String text, long senderId, long recipientId) throws ServiceException;

    ArrayList<UserMessage> findMessages(long userId) throws ServiceException;

    boolean haveUnreadMessages(long userId) throws ServiceException;

    void changeMessageStatus(long userId) throws ServiceException;

    void createNotificationForTrader(User dealer, Bet bet) throws ServiceException;
}
