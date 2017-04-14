package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 30.03.2017.
 */
public class OrderServiceImpl extends AbstractService implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public ArrayList<Order> getAllOrders() throws ServiceException {
        ArrayList<Order> orders = null;
        DaoHelper daoHelper = new DaoHelper();
        try {
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.initDao(orderDao);
            orders = orderDao.getAllOrders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return orders;
    }


    @Override
    public void addCancelledOrder(long lotId, long userId, BigDecimal payment) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.initDao(orderDao);
            orderDao.addCancelledOrder(lotId, userId, payment);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + "Exception during adding order into database");
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }

    }
}
