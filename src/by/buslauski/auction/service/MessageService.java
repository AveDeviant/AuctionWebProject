package by.buslauski.auction.service;

import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.service.exception.ServiceException;

import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface MessageService {
    String AUCTION_NOTIFICATION = "AUCTION RESULT";

    void addMessage(String theme, String text, long senderId, User recipient) throws ServiceException;

    ArrayList<UserMessage> findUserMessages(long userId) throws ServiceException;

    boolean haveUnreadMessages(long userId) throws ServiceException;

    void changeMessageStatus(long userId) throws ServiceException;

    void createMessageForTraderPurchaser(User dealer, Bet bet) throws ServiceException;
}
