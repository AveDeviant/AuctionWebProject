package by.buslauski.auction.dao;

import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.exception.DAOException;

import java.math.BigDecimal;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BankAccountDao {

    void addCard(long userId, String system, String cardNumber, BigDecimal moneyAmount) throws DAOException;

    BankCard findCardByNumber(String cardNumber) throws DAOException;

    BankCard findCardById(long cardId) throws DAOException;

    BankCard findCardByUserId(long userId) throws DAOException;

    boolean doPayment(long customerId, long recipientId, BigDecimal newBalanceCustomer,
                      BigDecimal newBalanceRecipient) throws DAOException;
}
