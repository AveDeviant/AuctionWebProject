package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.*;
import by.buslauski.auction.dao.impl.*;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.exception.ServiceException;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class AuctionServiceImpl extends AbstractService implements AuctionService {
    private static MessageService messageService = new MessageServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    /**
     * Check lots which bidding time is over but they already have confirmed bets.
     * Last bet is a winning bet - the last customer who made this bet is a winner of auction.
     * Set winning bets to customer for further ordering {@link User#winningBets}.
     *
     * @param user current user.
     * @throws ServiceException if DAOException ha been thrown
     *                          (database error occurs).
     */
    @Override
    public void setWinner(User user) throws ServiceException {
        ArrayList<Lot> lots = lotService.getLotsWithOverTiming();
        ArrayList<Bet> winningBets = new ArrayList<>();
        for (Lot lot : lots) {
            if (!lot.getBets().isEmpty() && lot.getAvailability()) {
                Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
                if (lastBet.getUserId() == user.getUserId()) {
                    winningBets.add(lastBet);
                }
            }
            user.setWinningBets(winningBets);
        }
    }

    @Override
    public void setWinner() throws ServiceException {
        lotService.getLotsWithOverTiming();
    }

    /**
     * Create notification for trader containing information about purchaser.
     * Withdraw lot from auction (update database setting {@link Lot#availability} to false).
     * Add {@link by.buslauski.auction.entity.Order} object into database for deal registration.
     * Create notification for trader and send it to trader's account in the system and
     * to trader e-mail box.
     *
     * @param bet the last bet to get fields to work with.
     * @return <tt>true</tt> - operation passed successfully (order was added, lot has been withdrawn and message was sent)
     * and <tt>false</tt> in other case.
     * @throws ServiceException if DAOException ha been thrown
     *                          (database error occurs).
     */
    @Override
    public boolean notifyTrader(Bet bet) throws ServiceException {
        boolean status = false;
        long customerId = bet.getUserId();
        long lotId = bet.getLotId();
        DaoHelper daoHelper = new DaoHelper();
        BigDecimal moneyAmount = bet.getBet();
        try {
            LotDao lotDao = new LotDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            UserDao userDao = new UserDaoImpl();
            daoHelper.beginTransaction(lotDao, orderDao, userDao);
            User trader = userDao.findTrader(bet.getLotId());
            // creating order with SUCCESS status
            orderDao.addOrder(customerId, trader.getUserId(), lotId, moneyAmount, true);
            lotDao.changeLotBiddingStatus(lotId, false);
            // create notification for customer, who offered lot
            messageService.createMessageForTraderPurchaser(trader, bet);
            daoHelper.commit();
            status = true;
        } catch (DAOException e) {
            daoHelper.rollback();
            LOGGER.log(Level.ERROR, e + " Exception during ordering.");
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
        }
        return status;
    }

    /**
     * Method is called in case rejecting payment by customer.
     * Lot returns in list of lots which available for bidding (modify {@link Lot#dateAvailable} and
     * set {@link Lot#availability} as <code>true</code>.
     * Cancelled {@link by.buslauski.auction.entity.Order} object added into database for further admin investigation.
     * Delete all {@link Bet} made for this lot.
     * Send notification to trader.
     * Block user in case hi is unreliable (count of rejected deals > {@link AuctionService#REJECTED_DEALS_COUNT})
     *
     * @param lot lot which must be returned to lot list.
     * @throws ServiceException if DAOException ha been thrown
     *                          (database error occurs).
     * @see by.buslauski.auction.action.impl.customer.RejectOrderImpl#execute(HttpServletRequest)
     * @see LotServiceImpl#getLotsWithOverTiming()
     */
    @Override
    public void resetBids(Lot lot) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
        BigDecimal currentPrice = lot.getCurrentPrice();
        long customerId = lastBet.getUserId();
        LocalDate date = addDaysToDate();
        try {
            BetDao betDao = new BetDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            NotificationDao notificationDao = new NotificationDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.beginTransaction(betDao, lotDao, orderDao, notificationDao);
            betDao.resetBets(lot.getId());
            lotDao.returnLotToBids(lot.getId(), lot.getPrice(), date);
            orderDao.addOrder(customerId, lot.getUserId(), lot.getId(), currentPrice, false);
            notificationDao.deleteNotification(lot.getId());
            daoHelper.commit();
            lot.getBets().clear(); // clear bet list
            UserService userService = new UserServiceImpl();
            userService.blockUser(AuctionService.REJECTED_DEALS_COUNT);  // trying to block user.
            NotificationService notificationService = new NotificationServiceImpl();
            notificationService.createNotificationDealRejected(lot);
        } catch (DAOException e) {
            daoHelper.rollback();
            LOGGER.log(Level.ERROR, e + " Exception during the cancellation of auction results.");
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    /**
     * Editing lot date before returning lot to bids.
     * Adding {@link AuctionService#WAITING_PERIOD} days to current date to get
     * a new {@link Lot#dateAvailable} to make the lot available for auction bids.
     *
     * @return {@link LocalDate} object.
     */
    private LocalDate addDaysToDate() {
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(AuctionService.WAITING_PERIOD);
        return date;
    }
}
