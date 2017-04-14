package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.MessageDao;
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
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messageDao.addMessage(theme, text, sender, recipient);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    @Override
    public ArrayList<UserMessage> findMessages(long userId) throws ServiceException {
        ArrayList<UserMessage> messages = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messages.addAll(messageDao.findUserMessages(userId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return messages;
    }

    @Override
    public boolean haveUnreadMessages(long userId) throws ServiceException {
        return countUserUnreadMessages(userId) > 0;
    }

    @Override
    public void changeMessageStatus(long userId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messageDao.changeMessageStatus(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }


    private long countUserUnreadMessages(long userId) throws ServiceException {
        long count = 0;
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            count = messageDao.countUserUnreadMessages(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return count;
    }

}
