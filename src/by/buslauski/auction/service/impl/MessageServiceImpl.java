package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.impl.MessageDaoImpl;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.MessageService;

import java.util.ArrayList;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageServiceImpl extends AbstractService implements MessageService {


    @Override
    public void addMessage(String theme, String text, User sender, User recipient) throws ServiceException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
        try {
            messageDao.addMessage(theme, text, sender, recipient);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
    }

    @Override
    public ArrayList<UserMessage> findMessages(long userId) throws ServiceException {
        ArrayList<UserMessage> messages = new ArrayList<>();
        MessageDaoImpl messageDao = new MessageDaoImpl();
        try {
            messages.addAll(messageDao.findUserMessages(userId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            messageDao.returnConnection();
        }
        return messages;
    }

    @Override
    public boolean haveUnreadMessages(long userId) throws ServiceException {
        return countUserUnreadMessages(userId) > 0;
    }

    @Override
    public void changeMessageStatus(long userId) throws ServiceException {
        MessageDaoImpl messageDao = new MessageDaoImpl();
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
        MessageDaoImpl messageDao = new MessageDaoImpl();
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
