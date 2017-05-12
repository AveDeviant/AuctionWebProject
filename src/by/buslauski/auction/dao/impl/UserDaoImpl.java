package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import org.apache.logging.log4j.Level;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 28.02.2017.
 */
public class UserDaoImpl extends AbstractDao implements UserDao {
    private static final String SQL_SELECT_USER = "SELECT user.id_user, user.id_role, name, username, email, password, city, address," +
            "phone_number, access, real_name, alias FROM user " +
            "JOIN role ON user.id_role=role.id_role WHERE username=?";
    private static final String SQL_SEARCH_EMAIl_ALIAS = "SELECT id_user from user WHERE email=? OR alias=?";
    private static final String SQL_INSERT_USER = "INSERT INTO user VALUES (NULL,?,?,?,?,NULL,NULL ,NULL,TRUE,NULL,?)";
    private static final String SQL_SELECT_ALL_USERS = "SELECT id_user, user.id_role, name, username, email, password, city, address, phone_number, access, real_name, alias " +
            "FROM user JOIN role ON user.id_role=role.id_role WHERE name='customer' ORDER BY id_user";
    private static final String SQL_SELECT_USER_BY_ID = "SELECT user.id_user, user.id_role, name, username, email, password, city, address, phone_number, access, real_name, alias " +
            "FROM user " +
            "JOIN role ON user.id_role=role.id_role " +
            "WHERE user.id_user=?";
    private static final String SQL_UPDATE_INFO = "UPDATE user SET real_name=?, city=?, address=?, phone_number=? WHERE id_user=?";
    private static final String SQL_CHANGE_USER_ACCESS = "UPDATE user SET access=? WHERE id_user=?";
    private static final String SQL_SELECT_ADMIN = "SELECT * FROM user JOIN role ON user.id_role=role.id_role " +
            "WHERE name='admin' LIMIT 1";
    private static final String SQL_SELECT_TRADER = "SELECT user.id_user, user.id_role, name, username, email, password," +
            " city, address, phone_number, access, real_name, alias " +
            "FROM user " +
            "JOIN role ON user.id_role=role.id_role " +
            "JOIN lot ON user.id_user=lot.id_user WHERE id_lot=?";
    private static final String SQL_UPDATE_TRADER_RATING = "INSERT INTO trader_rating VALUES (NULL,?,?,?)";
    private static final String SQL_SELECT_TRADER_RATING = "SELECT AVG(rating) AS rating FROM trader_rating WHERE id_trader=?";

    @Override
    public ArrayList<User> getAllCustomers() throws DAOException {
        ArrayList<User> customers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customers.add(initUser(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return customers;
    }

    @Override
    public String findPasswordByLogin(String username) throws DAOException {
        String password = "";
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USER)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return password;
            } else {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return password;
    }

    @Override
    public User findUserByLogin(String username) throws DAOException {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USER)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return user;
            } else {
                user = initUser(resultSet);
                return user;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public User findUserById(long userId) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = initUser(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return user;
    }

    @Override
    public void addUser(int id_role, String userName, String email, String password, String alias) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER)) {
            statement.setInt(1, id_role);
            statement.setString(2, userName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, alias);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void changeAccess(long userId, boolean access) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHANGE_USER_ACCESS)) {
            preparedStatement.setBoolean(1, access);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean findUserByEmailAlias(String email, String alias) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SEARCH_EMAIl_ALIAS)) {
            statement.setString(1, email);
            statement.setString(2, alias);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return true;
    }

    @Override
    public void updateUserInfo(long userId, String realName, String city, String address, String phone) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_INFO)) {
            preparedStatement.setString(1, realName);
            preparedStatement.setString(2, city);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setLong(5, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    @Override
    public User findAdmin() throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ADMIN)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = initUser(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return user;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public User findTrader(long lotId) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_TRADER)) {
            preparedStatement.setLong(1, lotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = initUser(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return user;
    }

    @Override
    public void updateTraderRating(long traderId, long customerId, int rating) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TRADER_RATING)) {
            preparedStatement.setLong(1, traderId);
            preparedStatement.setLong(2, customerId);
            preparedStatement.setInt(3, rating);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    @Override
    public double findTraderRating(long traderId) throws DAOException {
        double rating = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_TRADER_RATING)) {
            preparedStatement.setLong(1, traderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rating = resultSet.getDouble("rating");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return rating;
    }

    private User initUser(ResultSet resultSet) throws SQLException {
        Role role;
        if (Role.ADMIN.getValue().equals(resultSet.getString("name"))) {
            role = Role.ADMIN;
        } else {
            role = Role.CUSTOMER;
        }
        User user = new User(resultSet.getLong("id_user"),
                resultSet.getInt("id_role"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getBoolean("access"),
                role);
        user.setAlias(resultSet.getString("alias"));
        user.setName(resultSet.getString("real_name"));
        user.setAddress(resultSet.getString("address"));
        user.setCity(resultSet.getString("city"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        return user;
    }

}
