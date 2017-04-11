package by.buslauski.auction.service;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.BankAccountDao;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.util.AmountGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by Acer on 20.03.2017.
 */
public class BankService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger();
    private AmountGenerator amountGenerator = new AmountGenerator();

    public BankCard addAccount(long userId, String system, String cardNumber) throws ServiceException {
        BankAccountDao bankAccountDao = new BankAccountDao();
        BankCard bankCard = null;
        try {
            if (checkNumberForUnique(cardNumber)) {
                BigDecimal moneyAmount = amountGenerator.generateMoneyAmount();
                bankAccountDao.addCard(userId, system, cardNumber, moneyAmount);
                bankCard = bankAccountDao.findCardByNumber(cardNumber);
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e +" Exception during adding bank account data in database.");
            throw new ServiceException(e);
        } finally {
            bankAccountDao.returnConnection();
        }
        return bankCard;
    }

    /**
     * Check for uniqueness of card number
     *
     * @param cardNumber entered card number of the form XXXX-XXXX-XXXX-XXXX.
     * @return true - database doesn't store checking number
     * false - database contains checking number
     */
    private boolean checkNumberForUnique(String cardNumber) throws ServiceException {
        BankAccountDao bankAccountDao = new BankAccountDao();
        BankCard bankCard = null;
        try {
            bankCard = bankAccountDao.findCardByNumber(cardNumber);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            bankAccountDao.returnConnection();
        }
        return bankCard == null;
    }


    /**
     * Comparing balance on costumer's card and entered price.
     *
     * @param userId       user's ID
     * @param enteredPrice entered price
     * @return true - user's balance equal to or greater than entered price.
     * false - balance is less than entered price.
     */
    public boolean checkIsEnoughBalance(long userId, BigDecimal enteredPrice) throws ServiceException {
        BigDecimal balance = getBalance(userId);
        if (balance.compareTo(enteredPrice) < 0) {
            return false;
        }
        return true;
    }

    /**
     * Transfer from costumer's bank account to recipient.
     * Creating order with SUCCESS status.
     * Withdraw lot from bids (change field 'available' to false)
     *
     * @param bet
     * @return
     */
    public boolean doPayment(Bet bet) throws ServiceException {
        boolean success = true;
        long customerId = bet.getUserId();
        long lotId = bet.getLotId();
        BigDecimal moneyAmount = bet.getBet();
        ProxyConnection connection = null;
        try {
            connection = pool.takeConnectionFromPool();
            BankAccountDao bankAccountDao = new BankAccountDao(connection);
            LotDao lotDao = new LotDao(connection);
            OrderDao orderDao = new OrderDao(connection);
            bankAccountDao.setAutoCommitFalse();
            BankCard bankCardRecipient = findRecipient(lotId);
            BigDecimal customerBalance = bankAccountDao.findCardByUserId(customerId).getMoneyAmount();
            long recipientId = bankCardRecipient.getUserId();
            BigDecimal newBalanceCustomer = customerBalance.subtract(moneyAmount);  // calculate customer's new balance
            BigDecimal newBalanceRecipient = bankCardRecipient.getMoneyAmount();
            newBalanceRecipient = newBalanceRecipient.add(moneyAmount);          // calculate recipient's balance
            success = bankAccountDao.doPayment(customerId, recipientId, newBalanceCustomer, newBalanceRecipient);
            orderDao.createOrder(customerId, lotId, moneyAmount); // creating order
            lotDao.withdrawLot(lotId);   //withdraw lot from bids - set available to false.
            bankAccountDao.commit();
        } catch (DAOException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.log(Level.ERROR, e1);
            }
            LOGGER.log(Level.ERROR, e + " Exception during paying.");
            throw new ServiceException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, e);
                }
                returnConnection(connection);
            }
        }
        return success;
    }

    private BigDecimal getBalance(long userId) throws ServiceException {
        BigDecimal balance = null;
        BankAccountDao bankAccountDao = new BankAccountDao();
        try {
            balance = bankAccountDao.findCardByUserId(userId).getMoneyAmount();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            bankAccountDao.returnConnection();
        }
        return balance;
    }

    private BankCard findRecipient(long lotId) throws DAOException {
        BankAccountDao bankAccountDao = new BankAccountDao();
        BankCard bankCard = bankAccountDao.findRecipientAccount(lotId);
        bankAccountDao.returnConnection();
        return bankCard;
    }
}
