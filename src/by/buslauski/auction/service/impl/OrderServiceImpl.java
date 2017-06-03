package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.entity.AuctionStat;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.OrderService;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class OrderServiceImpl extends AbstractService implements OrderService {

    /**
     * Method gets all deals (confirmed or rejected) from database.
     *
     * @return defined {@link ArrayList} object containing {@link Order} objects.
     * @throws ServiceException in case DAOException has been thrown (database error occurs).
     * @see by.buslauski.auction.action.impl.GetOrdersCommandImpl#execute(HttpServletRequest)
     */
    @Override
    public ArrayList<Order> getAllOrders() throws ServiceException {
        ArrayList<Order> orders = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.initDao(orderDao);
            orders.addAll(orderDao.getAllOrders());
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return orders;
    }


    @Override
    public ArrayList<Order> getUserConfirmedOrders(long userId) throws ServiceException {
        ArrayList<Order> orders = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.initDao(orderDao);
            orders.addAll(orderDao.getUserConfirmedOrders(userId));
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return orders;
    }

    /**
     * Get from database total sum of {@link Order#payment} and total count of {@link Order} objects where
     * {@link Order#accept} is <code>true</code>.
     *
     * @return defined {@link AuctionStat} object.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Override
    public AuctionStat calculateStatistic() throws ServiceException {
        AuctionStat auctionStat = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.initDao(orderDao);
            auctionStat = orderDao.calculateStatistic();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return auctionStat;
    }

}
