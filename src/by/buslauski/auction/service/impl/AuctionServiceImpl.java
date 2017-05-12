package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.*;
import by.buslauski.auction.dao.impl.LotDaoImpl;
import by.buslauski.auction.dao.impl.MessageDaoImpl;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.dao.impl.UserDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.MessageService;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 10.05.2017.
 */
public class AuctionServiceImpl extends AbstractService implements AuctionService {
    private static MessageService messageService = new MessageServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    /**
     * Check lots which bidding time is over but already have confirmed bets.
     * Last bet is a winning bet - the last customer who made this bet is a winner of auction.
     * Add winning bets in consumer's ArrayList for further ordering.
     *
     * @param user auction customer.
     */
    @Override
    public void setWinner(User user) throws ServiceException {
        ArrayList<Lot> lots = lotService.getLotsWithOverTiming();
        ArrayList<Bet> winningBets = new ArrayList<>();
        for (Lot lot : lots) {
            if (!lot.getBets().isEmpty() && lot.getAvailability()) {   // if lot has confirmed bets
                Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
                if (lastBet.getUserId() == user.getUserId()) {
                    winningBets.add(lastBet);
                }
            }
            user.setWinningBets(winningBets);
        }
    }

    /**
     * Create notification for trader containing information about purchaser .
     * Withdraw lot from auction (update database setting {@link Lot#availability} to false).
     * Add order into database for deal registration.
     * Create notification for trader and send it to trader's account in the system and
     * to trader e-mail box.
     *
     * @param bet the last bet to get fields to work with.
     * @return true - operation passed successfully (order was added, lot has been withdrawn and message was sent)
     * and false in other case.
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
            // create notification for user, who offered lot
            messageService.createMessageForTraderAboutPurchaser(trader, bet);
            daoHelper.commit();
            status = true;

        } catch (DAOException e) {
            daoHelper.rollback();
            LOGGER.log(Level.ERROR, e + " Exception during paying.");
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
        }
        return status;
    }
}
