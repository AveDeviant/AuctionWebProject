package by.buslauski.auction.service;

import by.buslauski.auction.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface NotificationService {

    void createNotificationForTraderAuctionResult(long lotId, long traderId, long customerId) throws ServiceException;

}
