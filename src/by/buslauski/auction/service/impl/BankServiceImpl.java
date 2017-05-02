package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.BankAccountDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.dao.impl.BankAccountDaoImpl;
import by.buslauski.auction.dao.impl.LotDaoImpl;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.BankService;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.util.AmountGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by Acer on 20.03.2017.
 */
public class BankServiceImpl extends AbstractService implements BankService {
    private static UserService userService = new UserServiceImpl();
    private static MessageService messageService = new MessageServiceImpl();

    @Override
    public BankCard addAccount(long userId, String system, String cardNumber) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        BankAccountDao bankAccountDao = new BankAccountDaoImpl();
        daoHelper.initDao(bankAccountDao);
        try {
            BankCard bankCard = null;
            if (checkNumberForUnique(cardNumber)) {
                BigDecimal moneyAmount = AmountGenerator.generateMoneyAmount();
                bankAccountDao.addCard(userId, system, cardNumber, moneyAmount);
                bankCard = bankAccountDao.findCardByNumber(cardNumber);
            }
            return bankCard;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during adding bank account data in database.");
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Comparing balance on costumer's card and entered price.
     *
     * @param userId       user's ID
     * @param enteredPrice entered price
     * @return true - user's balance equals to or greater than entered price.
     * false - balance is less than entered price.
     */
    @Override
    public boolean checkIsEnoughBalance(long userId, BigDecimal enteredPrice) throws ServiceException {
        BigDecimal balance = getBalance(userId);
        return balance.compareTo(enteredPrice) >= 0;
    }

    /**
     * If auction is owner of lot than transfer money from customer's to recipient bank account.
     * In other case creating notification for trader about auction result.
     * Creating order with SUCCESS status.
     * Withdraw lot from bids (change field 'available' to false)
     *
     * @param bet of the customer
     * @return operation success
     */
    @Override
    public boolean doPayment(Bet bet) throws ServiceException {
        long customerId = bet.getUserId();
        long lotId = bet.getLotId();
        DaoHelper daoHelper = new DaoHelper();
        BigDecimal moneyAmount = bet.getBet();
        try {
            BankAccountDao bankAccountDao = new BankAccountDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.beginTransaction(bankAccountDao, lotDao, orderDao);
            User dealer = userService.findTrader(bet.getLotId());
            if (dealer.getRole() != Role.ADMIN) {      //if auction doesn't own the lot
                messageService.createNotificationForTrader(dealer, bet);  // create notification for user, who offered lot
                orderDao.addOrder(customerId, dealer.getUserId(), lotId, moneyAmount, true); // creating order with SUCCESS status
                lotDao.changeLotBiddingStatus(lotId, false);
                daoHelper.commit();
                return true;
            } else { // payment
                BankCard auctionAccount = bankAccountDao.findCardByUserId(dealer.getUserId());
                BigDecimal customerBalance = bankAccountDao.findCardByUserId(customerId).getMoneyAmount();
                BigDecimal newBalanceCustomer = customerBalance.subtract(moneyAmount);  // calculate customer's new balance
                BigDecimal newBalanceRecipient = auctionAccount.getMoneyAmount();
                newBalanceRecipient = newBalanceRecipient.add(moneyAmount);          // calculate recipient's balance
                boolean success = bankAccountDao.doPayment(customerId, dealer.getUserId(), newBalanceCustomer, newBalanceRecipient);
                orderDao.addOrder(customerId, dealer.getUserId(), lotId, moneyAmount, true); // creating order
                lotDao.changeLotBiddingStatus(lotId, false);   //withdraw lot from bids - set available to false.
                daoHelper.commit();
                return success;
            }
        } catch (DAOException e) {
            daoHelper.rollback();
            LOGGER.log(Level.ERROR, e + " Exception during paying.");
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    private BigDecimal getBalance(long userId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();

        BankAccountDao bankAccountDao = new BankAccountDaoImpl();
        daoHelper.initDao(bankAccountDao);

        try {
            return bankAccountDao.findCardByUserId(userId).getMoneyAmount();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Check card number for uniqueness.
     *
     * @param cardNumber entered card number of the form XXXX-XXXX-XXXX-XXXX.
     * @return true - database doesn't store checking number
     * false - database store checking number
     */
    private boolean checkNumberForUnique(String cardNumber) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();

        BankAccountDao bankAccountDao = new BankAccountDaoImpl();
        daoHelper.initDao(bankAccountDao);

        try {
            return bankAccountDao.findCardByNumber(cardNumber) == null;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }
}

