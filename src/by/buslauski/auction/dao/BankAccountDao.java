package by.buslauski.auction.dao;

import by.buslauski.auction.exception.DAOException;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BankAccountDao {

    void addCard(long userId, String system, String cardNumber, BigDecimal moneyAmount) throws DAOException;

    boolean doPayment(long customerId, long recipientId, BigDecimal newBalanceCustomer,
                      BigDecimal newBalanceRecipient) throws SQLException, DAOException;
}
