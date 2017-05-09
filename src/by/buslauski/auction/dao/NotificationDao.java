package by.buslauski.auction.dao;

import by.buslauski.auction.entity.AuctionNotification;
import by.buslauski.auction.exception.DAOException;

/**
 * Created by Acer on 08.05.2017.
 */
public interface NotificationDao {

    void createAuctionNotification(long lotId, long traderId, long customerId) throws DAOException;

    int countAuctionNotificationsAboutLot(long lotId, long customerId) throws DAOException;

    AuctionNotification findNotificationByLot(long lotId) throws DAOException;

    void deleteNotification(long lotId) throws DAOException;
}
