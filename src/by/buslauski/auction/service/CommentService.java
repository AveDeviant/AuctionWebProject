package by.buslauski.auction.service;

import by.buslauski.auction.service.exception.ServiceException;

/**
 * @author Buslauski Mikita
 */
public interface CommentService {

    void addComment(long userId, long lotId, String content) throws ServiceException;
}
