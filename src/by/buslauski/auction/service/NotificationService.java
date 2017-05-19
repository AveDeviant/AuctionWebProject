package by.buslauski.auction.service;

import by.buslauski.auction.service.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface NotificationService {

    void createNotificationAuctionResult(long lotId, long traderId, long customerId) throws ServiceException;

}
