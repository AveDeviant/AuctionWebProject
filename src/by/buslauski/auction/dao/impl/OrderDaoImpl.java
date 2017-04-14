package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.util.DateTimeParser;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 30.03.2017.
 */
public class OrderDaoImpl extends AbstractDao implements OrderDao {
    private static final String SQL_SELECT_ALL_ORDERS = "SELECT id_order, order.id_user, id_lot, payment, date, accept," +
            " real_name, city, address, phone_number FROM auction.order JOIN user ON order.id_user=user.id_user ORDER BY id_order";
    private static final String SQL_ADD_CANCELLED_ORDER = "INSERT INTO auction.order VALUES(NULL,?,?,?,NOW(),FALSE)";
    private static final String SQL_CREATE_ORDER = "INSERT INTO auction.order VALUES(NULL,?,?,?,NOW(),?)";


    @Override
    public void addCancelledOrder(long lotId, long userId, BigDecimal payment) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_CANCELLED_ORDER)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, lotId);
            preparedStatement.setBigDecimal(3, payment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }
    }

    @Override
    public void addOrder(long customerId, long lotId, BigDecimal price) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_ORDER)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, lotId);
            preparedStatement.setBigDecimal(3, price);
            preparedStatement.setBoolean(4, true);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }
    }

    @Override
    public ArrayList<Order> getAllOrders() throws DAOException {
        ArrayList<Order> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ORDERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(initOrder(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }
        return orders;
    }

    private Order initOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("id_order"));
        order.setUserId(resultSet.getLong("id_user"));
        order.setLotId(resultSet.getLong("id_lot"));
        order.setPayment(resultSet.getBigDecimal("payment"));
        order.setCostumerName(resultSet.getString("real_name"));
        order.setCostumerCity(resultSet.getString("city"));
        order.setCostumerAddress(resultSet.getString("address"));
        order.setCostumerPhone(resultSet.getString("phone_number"));
        order.setAccept(resultSet.getBoolean("accept"));
        order.setDateTime(DateTimeParser.parseDate(resultSet.getString("date")));
        return order;
    }
}
