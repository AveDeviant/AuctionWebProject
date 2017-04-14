package by.buslauski.auction.dao.impl;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.UserDao;
import by.buslauski.auction.entity.BankCard;
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
    private static final String SQL_SELECT_USER_AND_BANK = "SELECT user.id_user, user.id_role, name, username, email, password, city, address," +
            "phone_number, access, real_name, id_account, system, card_number, money_amount FROM user LEFT JOIN account ON user.id_user=account.id_user " +
            "JOIN role ON user.id_role=role.id_role WHERE username=?";
    private static final String SQL_SEARCH_EMAIl = "SELECT id_user from user WHERE email=?";
    private static final String SQL_INSERT_USER = "INSERT INTO user VALUES (NULL,?,?,?,?,NULL,NULL ,NULL,?,NULL)";
    private static final String SQL_SELECT_ALL_USERS = "SELECT id_user, user.id_role, name, username, email, password, city, address, phone_number, access, real_name " +
            "FROM user JOIN role ON user.id_role=role.id_role WHERE name='customer' ORDER BY id_user";
    private static final String SQL_SELECT_USER_BY_ID = "SELECT id_user, user.id_role, name, username, email, password, city, address, phone_number, access, real_name " +
            "FROM user JOIN role ON user.id_role=role.id_role WHERE id_user=?";
    private static final String SQL_UPDATE_INFO = "UPDATE user SET real_name=?, city=?, address=?, phone_number=? WHERE id_user=?";
    private static final String SQL_CHANGE_USER_ACCESS = "UPDATE user SET access=? WHERE id_user=?";
    private static final String SQL_SELECT_ADMIN = "SELECT * FROM user JOIN role ON user.id_role=role.id_role " +
            "WHERE name='admin' LIMIT 1";

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
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USER_AND_BANK)) {
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
    public User findUserByUsername(String username) throws DAOException {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USER_AND_BANK)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return user;
            } else {
                user = initUser(resultSet);
                if (resultSet.getString("id_account") != null) {
                    user.setBankCard(initUserBankCard(resultSet));
                }
                return user;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

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
    public void addUser(int id_role, String userName, String email, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER)) {
            statement.setInt(1, id_role);
            statement.setString(2, userName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setBoolean(5, true);
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
    public boolean findUserByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SEARCH_EMAIl)) {
            statement.setString(1, email);
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

    private BankCard initUserBankCard(ResultSet resultSet) throws SQLException {
        BankCard bankCard = new BankCard();
        bankCard.setCardId(resultSet.getLong("id_account"));
        bankCard.setUserId(resultSet.getLong("id_user"));
        bankCard.setCardSystem(resultSet.getString("system"));
        bankCard.setCardNumber(resultSet.getString("card_number"));
        bankCard.setMoneyAmount(resultSet.getBigDecimal("money_amount"));
        return bankCard;
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
        return user;
    }

}
