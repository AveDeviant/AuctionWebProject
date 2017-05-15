package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.OrderService;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class OrderServiceImpl extends AbstractService implements OrderService {

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

}
