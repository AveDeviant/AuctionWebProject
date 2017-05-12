package by.buslauski.auction.service;

import by.buslauski.auction.exception.ServiceException;

/**
 * Created by Acer on 08.05.2017.
 */
public interface NotificationService {

    void createNotificationForTraderAuctionResult(long lotId, long traderId, long customerId) throws ServiceException;

}
