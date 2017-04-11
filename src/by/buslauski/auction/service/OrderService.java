package by.buslauski.auction.service;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 30.03.2017.
 */
public class OrderService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger();

    public ArrayList<Order> getAllOrders() throws ServiceException {
        ArrayList<Order> orders = null;
        OrderDao orderDao = new OrderDao();
        try {
            orders = orderDao.getAllOrders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            orderDao.returnConnection();
        }
        return orders;
    }


    public void addCancelledOrder(long lotId, long userId, BigDecimal payment) throws ServiceException {
        OrderDao orderDao = new OrderDao();
        try {
            orderDao.addCancelledOrder(lotId, userId, payment);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + "Exception during adding order into database");
            throw new ServiceException(e);
        } finally {
            orderDao.returnConnection();
        }

    }
}
