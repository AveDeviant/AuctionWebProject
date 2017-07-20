package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.MessageDao;
import by.buslauski.auction.dao.impl.MessageDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.mail.MailSender;
import by.buslauski.auction.mail.exception.MailException;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class MessageServiceImpl extends AbstractService implements MessageService {
    private static UserService userService = new UserServiceImpl();


    @Override
    public void addMessage(String theme, String text, long senderId, User recipient) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messageDao.addMessage(theme, text, senderId, recipient.getUserId());
            sendMessageOnMailBox(theme, text, recipient);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Get <code>ArrayList</code> of {@link UserMessage} objects where specified user is a sender or a recipient of
     * message.
     *
     * @param userId ID of the specified user.
     * @return defined {@link ArrayList} object.
     * @throws ServiceException in case {@link DAOException} has been thrown
     *                          (database error occurs).
     */
    @Override
    public ArrayList<UserMessage> findUserMessages(long userId) throws ServiceException {
        ArrayList<UserMessage> messages = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messages.addAll(messageDao.findUserMessages(userId));
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
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

    /**
     * Changing message "isRead" status from <tt>false</tt> to <tt>true</tt>
     * by updating specified field in "message" table.
     *
     * @param userId ID of user who has read all incoming messages.
     * @throws ServiceException in case DAOException has been thrown (database error occurs).
     */
    @Override
    public void changeMessageStatus(long userId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            messageDao.changeMessageStatus(userId);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    @Override
    public void createMessageForTraderPurchaser(User trader, Bet bet) throws ServiceException {
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
        addMessage(AUCTION_NOTIFICATION, messageContent.toString(), customer.getUserId(), trader);
    }

    /**
     * Send message to user's e-mail box.
     *
     * @param theme     message theme;
     * @param content   message content;
     * @param recipient recipient of the message.
     */
    static void sendMessageOnMailBox(String theme, String content, User recipient) {
        new Thread() {
            @Override
            public void run() {
                try {
                    MailSender.sendMessage(theme, content, recipient.getEmail());
                } catch (MailException e) {
                    LOGGER.log(Level.ERROR, e.getMessage());
                }
            }
        }.start();
    }

    /**
     * Count amount of a new messages sent to user.
     *
     * @param userId ID of specified user.
     * @return messages count.
     * @throws ServiceException in case {@link DAOException} has been thrown
     *                          (database error occurs).
     */
    private long countUserUnreadMessages(long userId) throws ServiceException {
        long count = 0;
        DaoHelper daoHelper = new DaoHelper();
        try {
            MessageDao messageDao = new MessageDaoImpl();
            daoHelper.initDao(messageDao);
            count = messageDao.countUserUnreadMessages(userId);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return count;
    }

}
