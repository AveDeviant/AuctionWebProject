package by.buslauski.auction.service;

import by.buslauski.auction.dao.MessageDao;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;

import java.util.ArrayList;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageService extends AbstractService {


    public void addMessage(String theme, String text, User sender, User recipient) throws ServiceException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.addMessage(theme, text, sender, recipient);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
    }

    public ArrayList<UserMessage> findMessages(long userId) throws ServiceException {
        ArrayList<UserMessage> messages = new ArrayList<>();
        MessageDao messageDao = new MessageDao();
        try {
            messages.addAll(messageDao.findMessages(userId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
        return messages;
    }

    public boolean haveUnreadMessages(long userId) throws ServiceException {
        return countUserUnreadMessages(userId) > 0;
    }

    public void changeMessageStatus(long userId) throws ServiceException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.changeMessageStatus(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
    }


    private long countUserUnreadMessages(long userId) throws ServiceException {
        long count = 0;
        MessageDao messageDao = new MessageDao();
        try {
            count = messageDao.countUserUnreadMessages(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
        return count;
    }

}
