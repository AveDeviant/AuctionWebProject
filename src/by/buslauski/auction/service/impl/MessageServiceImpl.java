package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.MessageDao;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.dao.impl.MessageDaoImpl;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.mail.MailSender;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageServiceImpl extends AbstractService implements MessageService {
    private static final String AUCTION_NOTIFICATION = "AUCTION RESULT";
    private static UserService userService = new UserServiceImpl();


    @Override
    public void addMessage(String theme, String text, long senderId, long recipientId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messageDao.addMessage(theme, text, senderId, recipientId);
            sendMessageOnMailBox(theme, text, recipientId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    @Override
    public ArrayList<UserMessage> findUserMessages(long userId) throws ServiceException {
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

    @Override
    public void createMessageForTraderAboutPurchaser(User dealer, Bet bet) throws ServiceException {
        StringBuilder messageContent = new StringBuilder();
        User customer = userService.findUserById(bet.getUserId());
        messageContent.append("Title: ").append(bet.getLotTitle());
        messageContent.append("\n");
        messageContent.append("Price: ").append(bet.getBet());
        messageContent.append("\n");
        messageContent.append("Purchaser: ").append(customer.getName());
        messageContent.append("\n");
        messageContent.append("Address: ").append(customer.getCity()).append(", ").append(customer.getAddress());
        messageContent.append("\n");
        messageContent.append("E-mail: ").append(customer.getEmail());
        messageContent.append("\n");
        messageContent.append("Phone number: ").append(customer.getPhoneNumber());
        addMessage(AUCTION_NOTIFICATION, messageContent.toString(), customer.getUserId(), dealer.getUserId());
    }

    private void sendMessageOnMailBox(String theme, String content, long recipientId) {
        UserDao userDao = new UserDaoImpl();
        DaoHelper daoHelper = new DaoHelper();
        try {
            daoHelper.initDao(userDao);
            User recipient = userDao.findUserById(recipientId);
            if (recipient.getRole() != Role.ADMIN) {
                MailSender.sendMessage(theme, content, recipient.getEmail());
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + " Excepting during getting entity from database");
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
