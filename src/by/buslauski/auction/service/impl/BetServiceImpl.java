package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.impl.BetDaoImpl;
import by.buslauski.auction.dao.impl.LotDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.BetService;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mikita Buslauski
 */
public class BetServiceImpl extends AbstractService implements BetService {
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * Adding bet and updating lot current price.
     * <p>
     * <code>ReentrantLock</code> reduces performance
     * but also reduces the rick of incorrect logic behavior.
     *
     * @param userId customer iD.
     * @param lotId  lot ID.
     * @param price  new lot price.
     * @return true in case operation passed successfully and lot price has been updated,
     * false in other case.
     * @throws ServiceException if a database access error occurs
     *                          (DAOException has been thrown).
     */
    @Override
    public boolean addBet(long userId, long lotId, BigDecimal price) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        boolean flag = false;
        try {
            lock.lock();
            BetDao betDao = new BetDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            daoHelper.beginTransaction(betDao, lotDao);
            Lot lot = lotDao.findLotById(lotId);
            if (!checkBetValue(lot, price)) {
                return flag;
            }
            betDao.addBet(userId, lotId, price);
            lotDao.updateCurrentPrice(lotId, price);
            daoHelper.commit();
            flag = true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during adding bet to database");
            daoHelper.rollback();
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
            lock.unlock();
        }
        return flag;
    }

    /**
     * Check entered bet value.
     * <p>
     * The entered bet should greater than current lot price {@link Lot#currentPrice}
     * as a minimum of {@link by.buslauski.auction.service.AuctionService#AUCTION_STEP_PERCENT} percents
     * of the starting lot price {@link Lot#price}.
     *
     * @param lot lot which price should be updated.
     * @param bet entered bet.
     * @return true - entered bet is correct;
     * false  in other case.
     */
    @Override
    public boolean checkBetValue(Lot lot, BigDecimal bet) {
        BigDecimal startingPrice = lot.getPrice();
        BigDecimal step = startingPrice.divide(new BigDecimal(100), BigDecimal.ROUND_CEILING)
                .multiply(BigDecimal.valueOf(AuctionService.AUCTION_STEP_PERCENT));
        BigDecimal price = lot.getCurrentPrice();
        BigDecimal minPrice = price.add(step);
        return bet.compareTo(minPrice) >= 0;
    }

    /**
     * /**
     * Get <code>ArrayList</code> of {@link Bet} objects which were made by specified {@link User}.
     *
     * @param userId ID of specified user,
     * @return defined {@link ArrayList} object containing {@link Bet} objects.
     * @throws ServiceException in case DAOException has e]been thrown (database error occurs)
     */
    @Override
    public ArrayList<Bet> getUserBets(long userId) throws ServiceException {
        ArrayList<Bet> bets = new ArrayList<>();
        DaoHelper daoHelper = new DaoHelper();
        try {
            BetDao betDao = new BetDaoImpl();
            daoHelper.initDao(betDao);
            bets.addAll(betDao.getUserBets(userId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return bets;
    }


}
