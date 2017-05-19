package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.NotificationDao;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.dao.impl.NotificationDaoImpl;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.AuctionNotification;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.NotificationService;
import org.apache.logging.log4j.Level;

/**
 * @author Mikita Busaluski
 */
public class NotificationServiceImpl extends AbstractService implements NotificationService {
    private static final String NOTIFICATION = "AUCTION RESULT/РЕЗУЛЬТАТЫ ТОРГОВ";

    /**
     * Creating notification for trader that auction time for his lot is over
     * and trader has a purchaser.
     * Creating notification for customer that he is a winner of the auction.
     * Insert created notifications into database.
     * Send message with this notification to trader e-mail box.
     *
     * @param lotId      ID lot;
     * @param customerId purchaser ID;
     * @param traderId   trader ID;
     * @throws ServiceException in case DAOException has been thrown (database error occurs)
     * @see MessageServiceImpl#addMessage(String, String, long, User)
     */
    @Override
    public void createNotificationAuctionResult(long lotId, long customerId, long traderId) throws ServiceException {
        NotificationDao notificationDao = new NotificationDaoImpl();
        UserDao userDao = new UserDaoImpl();
        MessageService messageService = new MessageServiceImpl();
        DaoHelper daoHelperNotification = new DaoHelper();
        DaoHelper daoHelperUser = new DaoHelper();
        try {
            daoHelperNotification.initDao(notificationDao);
            daoHelperUser.initDao(userDao);
            User trader = userDao.findUserById(traderId);
            User admin = userDao.findAdmin();
            User customer = userDao.findUserById(customerId);
            if (notificationDao.countAuctionNotificationsAboutLot(lotId, customerId) == 0) {
                notificationDao.createAuctionNotification(lotId, traderId, customerId);
                AuctionNotification notification = notificationDao.findNotificationByLot(lotId);
                String contentToTrader = initNotificationToTrader(notification);
                String contentToCustomer = initNotificationToCustomer(notification);
                messageService.addMessage(NOTIFICATION, contentToTrader, customerId, trader);
                messageService.addMessage(NOTIFICATION, contentToCustomer, admin.getUserId(), customer);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelperNotification.release();
            daoHelperUser.release();
        }
    }

    private String initNotificationToTrader(AuctionNotification notification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Auction time for lot ");
        stringBuilder.append(notification.getLotTitle());
        stringBuilder.append(" is over. The winner is ");
        stringBuilder.append(notification.getCustomerAlias());
        stringBuilder.append(". This customer must to confirm the transaction within 10 days, in other case all" +
                " result of the auction will be reset./");
        stringBuilder.append(" Торги по лоту ");
        stringBuilder.append(notification.getLotTitle());
        stringBuilder.append(" окончены. Торги выиграл пользователь с именем ");
        stringBuilder.append(notification.getCustomerAlias());
        stringBuilder.append(". Данный пользователь должен подтвердить сделку в течении 10 дней," +
                " иначе все результаты будут аннулированы.");
        return stringBuilder.toString();
    }

    private String initNotificationToCustomer(AuctionNotification notification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Congratulations! You are the winner of the auction!").append("\n");
        stringBuilder.append("Lot: ").append(notification.getLotTitle()).append("\n");
        stringBuilder.append("Please, log in on the site for confirmation of the deal.").append("\n");
        stringBuilder.append("Attention! If you exceed auction waiting period (10 days) then all results of the auction will be reset./");
        stringBuilder.append("Поздравляем! Вы выиграли торги!").append("\n");
        stringBuilder.append("Лот: ").append(notification.getLotTitle()).append("\n");
        stringBuilder.append("Пожалуйста, авторизуйтесь на сайте и подтвердите сделку.").append("\n");
        stringBuilder.append("Внимание! Если Вы превысите срок ожидания подтверждения сделки (10 дней с момента окончания торгов), все результаты торгов будут аннулированы.");
        return stringBuilder.toString();
    }
}
