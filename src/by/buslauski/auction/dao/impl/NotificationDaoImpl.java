package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.NotificationDao;
import by.buslauski.auction.entity.AuctionNotification;
import by.buslauski.auction.dao.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mikita Buslauski
 */
public class NotificationDaoImpl extends AbstractDao implements NotificationDao {
    private static final String SQL_CREATE_AUCTION_NOTIFICATION = "INSERT INTO result_notification " +
            "VALUES(NULL,?,?,?,NOW())";
    private static final String SQL_CHECK_FOR_EXISTING_NOTIFICATION = "SELECT COUNT(*) AS count FROM result_notification " +
            "WHERE id_customer=? AND id_lot=?";
    private static final String SQL_DELETE_NOTIFICATION = "DELETE FROM result_notification WHERE id_lot=?";
    private static final String SQL_SELECT_NOTIFICATION = "SELECT id_notification, result.id_customer, customer.alias, " +
            "trader.alias, result.id_lot, lot.title, date, result.id_trader " +
            "FROM result_notification AS result " +
            "JOIN user AS customer ON result.id_customer=customer.id_user " +
            "JOIN user AS trader ON result.id_trader=trader.id_user " +
            "JOIN lot ON result.id_lot=lot.id_lot " +
            "WHERE result.id_lot=?";

    public void createAuctionNotification(long lotId, long traderId, long customerId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_AUCTION_NOTIFICATION)) {
            preparedStatement.setLong(1, traderId);
            preparedStatement.setLong(2, customerId);
            preparedStatement.setLong(3, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public int countAuctionNotificationsAboutLot(long id_lot, long id_customer) throws DAOException {
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_FOR_EXISTING_NOTIFICATION)) {
            preparedStatement.setLong(1, id_customer);
            preparedStatement.setLong(2, id_lot);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return count;
    }

    @Override
    public AuctionNotification findNotificationByLot(long lotId) throws DAOException {
        AuctionNotification auctionNotification = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_NOTIFICATION)) {
            preparedStatement.setLong(1, lotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                auctionNotification = initAuctionNotification(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return auctionNotification;
    }

    @Override
    public void deleteNotification(long lotId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_NOTIFICATION)) {
            preparedStatement.setLong(1, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private AuctionNotification initAuctionNotification(ResultSet resultSet) throws SQLException {
        AuctionNotification notification = new AuctionNotification();
        notification.setNotificationId(resultSet.getLong("id_notification"));
        notification.setCustomerId(resultSet.getLong("result.id_customer"));
        notification.setTraderId(resultSet.getLong("result.id_trader"));
        notification.setLotId(resultSet.getLong("result.id_lot"));
        notification.setLotTitle(resultSet.getString("lot.title"));
        notification.setCustomerAlias(resultSet.getString("customer.alias"));
        return notification;
    }
}
