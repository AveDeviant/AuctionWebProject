package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.NotificationDao;
import by.buslauski.auction.dao.impl.NotificationDaoImpl;
import by.buslauski.auction.entity.AuctionNotification;
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


    @Override
    public void createNotificationForTraderAuctionResult(long lotId, long customerId, long traderId) throws ServiceException {
        NotificationDao notificationDao = new NotificationDaoImpl();
        MessageService messageService= new MessageServiceImpl();
        DaoHelper daoHelper = new DaoHelper();
        try {
            daoHelper.initDao(notificationDao);
            if (notificationDao.countAuctionNotificationsAboutLot(lotId, customerId) == 0) {
                notificationDao.createAuctionNotification(lotId, traderId, customerId);
                AuctionNotification notification = notificationDao.findNotificationByLot(lotId);
                String content = initNotificationContent(notification);
                messageService.addMessage(NOTIFICATION, content, customerId, traderId);
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
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
