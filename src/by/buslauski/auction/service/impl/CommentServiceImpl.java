package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.CommentDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.impl.CommentDaoImpl;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.CommentService;
import org.apache.logging.log4j.Level;

/**
 * @author Buslauski Mikita
 */
public class CommentServiceImpl extends AbstractService implements CommentService {


    @Override
    public void addComment(long userId, long lotId, String content) throws ServiceException {
        CommentDao commentDao = new CommentDaoImpl();
        DaoHelper daoHelper = new DaoHelper();
        daoHelper.initDao(commentDao);
        try {
            commentDao.addComment(userId,lotId,content);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR,e + " Exception during adding comment to database.");
            throw new ServiceException(e);
        }
        finally {
            daoHelper.release();
        }
    }
}
