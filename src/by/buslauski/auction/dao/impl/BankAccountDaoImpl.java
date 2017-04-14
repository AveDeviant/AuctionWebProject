package by.buslauski.auction.dao.impl;

import by.buslauski.auction.connection.ProxyConnection;
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
    private static final String SQL_FIND_RECIPIENT_ACCOUNT = "SELECT id_account, account.id_user, title, system, card_number, money_amount " +
            "FROM account JOIN lot ON account.id_user=lot.id_user WHERE id_lot=?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE account SET money_amount=? WHERE id_user=?";


    public BankAccountDaoImpl() {
    }

    public BankAccountDaoImpl(ProxyConnection connection) {
        super(connection);
    }

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

    public BankCard findRecipientAccount(long lotId) throws DAOException {
        BankCard bankCard = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPIENT_ACCOUNT)) {
            preparedStatement.setLong(1, lotId);
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
    public boolean doPayment(long customerId, long recipientId, BigDecimal newBalanceCustomer,
                             BigDecimal newBalanceRecipient) throws SQLException, DAOException {
        boolean success = false;
        PreparedStatement preparedStatementRecipient = null;
        try (PreparedStatement preparedStatementCustomer = connection.prepareStatement(SQL_UPDATE_ACCOUNT)) {
            preparedStatementCustomer.setBigDecimal(1, newBalanceCustomer);
            preparedStatementCustomer.setLong(2, customerId);
            preparedStatementRecipient = connection.prepareStatement(SQL_UPDATE_ACCOUNT);
            preparedStatementRecipient.setBigDecimal(1, newBalanceRecipient);
            preparedStatementRecipient.setLong(2, recipientId);
            preparedStatementCustomer.executeUpdate();
            preparedStatementRecipient.executeUpdate();
            success = true;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        } finally {
            if (preparedStatementRecipient != null) {
                preparedStatementRecipient.close();
            }
        }
        return success;
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
