package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.NotificationDao;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.dao.impl.NotificationDaoImpl;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.AuctionNotification;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.NotificationService;
import org.apache.logging.log4j.Level;

/**
 * Created by Acer on 08.05.2017.
 */
public class NotificationServiceImpl extends AbstractService implements NotificationService {
    private static final String NOTIFICATION = "AUCTION RESULT/РЕЗУЛЬТАТЫ ТОРГОВ";


    /**
     * Creating notification to trader that auction time for his lot is over
     * and trader has a purchaser.
     * Insert created notification into database.
     * Send message with this notification to trader e-mail box.
     *
     * @param lotId      ID lot,
     * @param customerId purchaser ID
     * @param traderId   trader ID
     * @throws ServiceException in case DAOException has been thrown (database error occurs)
     */
    @Override
    public void createNotificationForTraderAuctionResult(long lotId, long customerId, long traderId) throws ServiceException {
        NotificationDao notificationDao = new NotificationDaoImpl();
        UserDao userDao = new UserDaoImpl();
        MessageService messageService = new MessageServiceImpl();
        DaoHelper daoHelperNotification = new DaoHelper();
        DaoHelper daoHelperUser = new DaoHelper();
        try {
            daoHelperNotification.initDao(notificationDao);
            daoHelperUser.initDao(userDao);
            User trader = userDao.findUserById(traderId);
            if (notificationDao.countAuctionNotificationsAboutLot(lotId, customerId) == 0) {
                notificationDao.createAuctionNotification(lotId, traderId, customerId);
                AuctionNotification notification = notificationDao.findNotificationByLot(lotId);
                String content = initNotificationContent(notification);
                messageService.addMessage(NOTIFICATION, content, customerId, trader);
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelperNotification.release();
            daoHelperUser.release();
        }
    }

    private String initNotificationContent(AuctionNotification notification) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Auction time for lot ");
        stringBuilder.append(notification.getLotTitle());
        stringBuilder.append(" is over. The winner is ");
        stringBuilder.append(notification.getCustomerAlias());
        stringBuilder.append(". This user must to confirm the transaction within 10 days, in other case all" +
                " result of the auction will be reset./");
        stringBuilder.append(" Торги по лоту ");
        stringBuilder.append(notification.getLotTitle());
        stringBuilder.append(" окончены. Торги выиграл пользователь с именем ");
        stringBuilder.append(notification.getCustomerAlias());
        stringBuilder.append(". Данный пользователь должен подтвердить сделку в течение 10 дней," +
                " иначе все результаты будут аннулированы.");
        return stringBuilder.toString();
    }
}
