package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.BankAccountDao;
import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.exception.DAOException;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Acer on 20.03.2017.
 */
public class BankAccountDaoImpl extends AbstractDao implements BankAccountDao {
    private static final String SQL_ADD_CARD = "INSERT INTO account VALUES(NULL,?,?,?,?)";
    private static final String SQL_FIND_CARD_BY_NUMBER = "SELECT id_account, id_user, system, card_number, money_amount " +
            "FROM account where card_number=?";
    private static final String SQL_FIND_CARD_BY_ID = "SELECT id_account, id_user, system, card_number, money_amount " +
            "FROM account where id_account=?";
    private static final String SQL_FIND_CARD_BY_USER = "SELECT id_account, id_user, system, card_number, money_amount " +
            "FROM account where id_user=?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE account SET money_amount=? WHERE id_user=?";

    @Override
    public void addCard(long userId, String system, String cardNumber, BigDecimal moneyAmount) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_CARD)) {
            statement.setLong(1, userId);
            statement.setString(2, system);
            statement.setString(3, cardNumber);
            statement.setBigDecimal(4, moneyAmount);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
    }

    @Override
    public BankCard findCardByNumber(String cardNumber) throws DAOException {
        BankCard bankCard = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_CARD_BY_NUMBER)) {
            statement.setString(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bankCard = initBankCard(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
        return bankCard;
    }

    @Override
    public BankCard findCardById(long cardId) throws DAOException {
        BankCard bankCard = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CARD_BY_ID)) {
            preparedStatement.setLong(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bankCard = initBankCard(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
        return bankCard;
    }

    @Override
    public BankCard findCardByUserId(long userId) throws DAOException {
        BankCard bankCard = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CARD_BY_USER)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bankCard = initBankCard(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
        return bankCard;
    }

    @Override
    public boolean doPayment(long customerId, long recipientId, BigDecimal newBalanceCustomer, BigDecimal newBalanceRecipient) throws DAOException {
        try (PreparedStatement psCustomer = connection.prepareStatement(SQL_UPDATE_ACCOUNT);
             PreparedStatement psRecipient = connection.prepareStatement(SQL_UPDATE_ACCOUNT)) {

            psCustomer.setBigDecimal(1, newBalanceCustomer);
            psCustomer.setLong(2, customerId);
            psCustomer.executeUpdate();

            psRecipient.setBigDecimal(1, newBalanceRecipient);
            psRecipient.setLong(2, recipientId);
            psRecipient.executeUpdate();

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }

    }


    private BankCard initBankCard(ResultSet resultSet) throws SQLException {
        BankCard bankCard = new BankCard();
        bankCard.setCardId(resultSet.getLong("id_account"));
        bankCard.setUserId(resultSet.getLong("id_user"));
        bankCard.setCardSystem(resultSet.getString("system"));
        bankCard.setCardNumber(resultSet.getString("card_number"));
        bankCard.setMoneyAmount(resultSet.getBigDecimal("money_amount"));
        return bankCard;
    }


}
