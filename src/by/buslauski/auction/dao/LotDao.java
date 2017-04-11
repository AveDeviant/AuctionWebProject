package by.buslauski.auction.dao;


import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.omg.CORBA.PUBLIC_MEMBER;


import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Acer on 15.03.2017.
 */
public class LotDao extends AbstractDao {
    private static final String SQL_ADD_LOT = "INSERT INTO lot VALUES(NULL,?,?,?,?,?,?,?,DATE(?),?)";
    private static final String SQL_GET_AVAILABLE_LOTS = "SELECT id_lot, id_user, lot.id_category,name, title, photo, description, starting_price, date_available," +
            "current_price FROM lot JOIN category ON lot.id_category=category.id_category WHERE available=1 AND NOW()<date_available ORDER BY id_lot";
    private static final String SQL_GET_LOT_BY_ID = "SELECT id_lot, id_user, lot.id_category, name, title, photo, description, starting_price, available, date_available, " +
            "current_price FROM lot JOIN category ON lot.id_category=category.id_category WHERE id_lot=?";
    private static final String SQL_GET_LOTS_OVER_TIMING = "SELECT id_lot, id_user, lot.id_category, name, title, photo, description, starting_price,available, date_available," +
            "current_price FROM lot JOIN category ON lot.id_category=category.id_category WHERE NOW()>date_available";
    private static final String SQL_GET_ALL_LOTS = "SELECT id_lot, id_user, lot.id_category, name, title, photo, description, starting_price,available, date_available," +
            "current_price FROM lot JOIN category ON lot.id_category=category.id_category";
    private static final String SQL_EDIT_LOT = "UPDATE lot SET title=?, starting_price=?, available=?, photo=?, date_available=DATE(?), id_category=? WHERE id_lot=?";
    private static final String SQL_WITHDRAW_LOT = "UPDATE lot SET available=false WHERE id_lot=?";
    private static final String SQL_UPDATE_CURRENT_PRICE = "UPDATE lot SET current_price=? WHERE id_lot=?";
    private static final String SQL_RETURN_LOT_TO_BIDS = "UPDATE lot SET available=true, date_available=DATE(?)," +
            "current_price=? WHERE id_lot=?";
    private static final String SQL_DELETE_LOT = "DELETE FROM lot WHERE id_lot=?";


    public LotDao() {
    }

    public LotDao(ProxyConnection connection) {
        super(connection);
    }

    public void addLot(long userId, int categoryId, String title, String description,
                       String image, BigDecimal price, String availableDate,
                       boolean availability, BigDecimal currentPrice) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_LOT)) {
            statement.setLong(1, userId);
            statement.setInt(2, categoryId);
            statement.setString(3, title);
            statement.setString(4, image);
            statement.setString(5, description);
            statement.setBigDecimal(6, price);
            statement.setBoolean(7, availability);
            statement.setString(8, availableDate);
            statement.setBigDecimal(9, currentPrice);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e + "SQLException during adding lot in database");
            throw new DAOException(e);
        }
    }

    public ArrayList<Lot> findAllLots() throws DAOException {
        ArrayList<Lot> lots = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_LOTS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(initLot(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return lots;
    }

    public ArrayList<Lot> getAvailableLots() throws DAOException {
        ArrayList<Lot> lots = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_GET_AVAILABLE_LOTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lots.add(new Lot(resultSet.getLong("id_lot"),
                        resultSet.getLong("id_user"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("photo"),
                        resultSet.getInt("id_category"),
                        resultSet.getBigDecimal("starting_price"),
                        true,
                        LocalDate.parse(resultSet.getString("date_available")),
                        resultSet.getBigDecimal("current_price"),
                        resultSet.getString("name")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e + "SQLException during reading data from database");
            throw new DAOException(e);

        }
        return lots;
    }

    public Lot getLotById(long id) throws DAOException {
        Lot lot = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_GET_LOT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lot = initLot(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return lot;
    }

    public void updateCurrentPrice(long lotId, BigDecimal price) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CURRENT_PRICE)) {
            preparedStatement.setBigDecimal(1, price);
            preparedStatement.setLong(2, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    public ArrayList<Lot> getLotsOverTiming() throws DAOException {
        ArrayList<Lot> lots = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LOTS_OVER_TIMING)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(initLot(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return lots;
    }

    public void editLot(long lotId, int categoryId, String title, BigDecimal price, String image, boolean availability,
                        String availableDate) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_EDIT_LOT)) {
            preparedStatement.setString(1, title);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setBoolean(3, availability);
            preparedStatement.setString(4, image);
            preparedStatement.setString(5, availableDate);
            preparedStatement.setInt(6, categoryId);
            preparedStatement.setLong(7, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    public void returnLotToBids(long lotId, BigDecimal price, LocalDate date) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_RETURN_LOT_TO_BIDS)) {
            preparedStatement.setString(1, date.toString());
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setLong(3, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    public void withdrawLot(long lotId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_WITHDRAW_LOT)) {
            preparedStatement.setLong(1, lotId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    public boolean deleteLot(long lotId) throws DAOException {
        boolean success = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_LOT)) {
            preparedStatement.setLong(1, lotId);
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
        return success;
    }

    private Lot initLot(ResultSet resultSet) throws SQLException {
        Lot lot = new Lot(
                resultSet.getLong("id_lot"),
                resultSet.getLong("id_user"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("photo"),
                resultSet.getInt("id_category"),
                resultSet.getBigDecimal("starting_price"),
                resultSet.getBoolean("available"),
                LocalDate.parse(resultSet.getString("date_available")),
                resultSet.getBigDecimal("current_price"),
                resultSet.getString("name"));  // category name
        return lot;
    }

}

