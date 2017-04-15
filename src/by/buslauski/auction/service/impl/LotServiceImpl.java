package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.*;
import by.buslauski.auction.dao.impl.BetDaoImpl;
import by.buslauski.auction.dao.impl.CategoryDaoImpl;
import by.buslauski.auction.dao.impl.LotDaoImpl;
import by.buslauski.auction.dao.impl.OrderDaoImpl;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.validator.LotValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Created by Acer on 16.03.2017.
 */
public class LotServiceImpl extends AbstractService implements LotService {
    private static final int WAITING_PERIOD = 10;       // waiting period for registration of the won lot, in days.
    private static Logger LOGGER = LogManager.getLogger();


    @Override
    public void addLot(String title, long userId, String description,
                       String image, BigDecimal price, boolean availability,
                       String category, String availableDate) throws ServiceException {
        LocalDate date = LocalDate.parse(availableDate);
        DaoHelper daoHelperCategory = new DaoHelper();
        DaoHelper daoHelperLot = new DaoHelper();
        try {
            CategoryDaoImpl categoryDao = new CategoryDaoImpl();
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelperCategory.initDao(categoryDao);
            daoHelperLot.initDao(lotDao);
            if (LotValidator.checkLot(title, description, date)) {
                int categoryId = categoryDao.findCategoryIdByName(category);
                lotDao.addLot(userId, categoryId, title, description, image, price, availableDate, availability, price);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelperCategory.release();
            daoHelperLot.release();
        }

    }

    @Override
    public ArrayList<Lot> getAllLots() throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        ArrayList<Lot> lots = null;
        try {
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            lots = lotDao.findAllLots();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return lots;
    }

    /**
     * @return ArrayList of lots which available for bidding
     */
    @Override
    public ArrayList<Lot> getAvailableLots() throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        ArrayList<Lot> availableLots = null;
        try {
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            availableLots = lotDao.findAvailableLots();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return availableLots;
    }


    @Override
    public Lot getLotById(long lotId) throws ServiceException {
        DaoHelper daoHelperLot = new DaoHelper();
        DaoHelper daoHelperBet = new DaoHelper();
        Lot lot = null;
        try {
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelperLot.initDao(lotDao);
            lot = lotDao.findLotById(lotId);
            if (lot != null) {
                BetDaoImpl betDao = new BetDaoImpl();
                daoHelperBet.initDao(betDao);
                lot.setBets(betDao.getBetsByLotId(lotId)); // setting bets to lot
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelperLot.release();
            daoHelperBet.release();
        }
        return lot;
    }

    /**
     * @param lotId
     * @return
     * @throws ServiceException
     */
    @Override
    public Lot getAvailableLotById(long lotId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        Lot lot = null;
        try {
            ArrayList<Lot> lots = getAvailableLots();
            for (Lot availableLot : lots) {
                if (availableLot.getId() == lotId) {
                    lot = availableLot;
                }
            }
            if (lot != null) {
                BetDaoImpl betDao = new BetDaoImpl();
                daoHelper.initDao(betDao);
                lot.setBets(betDao.getBetsByLotId(lotId)); // setting bets to lot
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return lot;
    }

    /**
     * Method checks if the lot are still available for bidding.
     * Lot bidding time ends at 00:00:00
     *
     * @param lot checked lot.
     * @return true -  lot available for bidding
     * false - lot bidding time is over or lot was withdrawn from the auction
     */
    @Override
    public boolean checkActuality(Lot lot) {
        LocalDate lotDate = lot.getDateAvailable();
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(lotDate.atTime(0, 0, 0)) && lot.getAvailability();
    }

    /**
     * @return ArrayList containing lots which bidding time is over.
     */
    @Override
    public ArrayList<Lot> getLotsWithOverTiming() throws ServiceException {
        DaoHelper daoHelperLot = new DaoHelper();
        DaoHelper daoHelperBet = new DaoHelper();
        ArrayList<Lot> lots = null;
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelperLot.initDao(lotDao);
            lots = lotDao.findLotsWithOverTiming();
            if (!lots.isEmpty()) {
                BetDaoImpl betDao = new BetDaoImpl();
                daoHelperBet.initDao(betDao);
                for (Lot lot : lots) {
                    lot.getBets().addAll(betDao.getBetsByLotId(lot.getId())); // adding bets to the current lot.
                }
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelperBet.release();
            daoHelperLot.release();
        }
        return lots;
    }

    @Override
    public ArrayList<Lot> findLotsByCategory(String category) throws ServiceException {
        ArrayList<Lot> availableLots = getAvailableLots();
        ArrayList<Lot> lotsByCategory = new ArrayList<>();
        for (Lot lot : availableLots) {
            if (lot.getCategory().equals(category)) {
                lotsByCategory.add(lot);
            }
        }
        return lotsByCategory;
    }

    @Override
    public void editLot(long lotId, String category, String title, String image, BigDecimal price,
                        boolean availability, String availableDate) throws ServiceException {
        DaoHelper daoHelperCategory = new DaoHelper();
        DaoHelper daoHelperLot = new DaoHelper();
        LocalDate localDate = LocalDate.parse(availableDate);
        try {
            CategoryDao categoryDao = new CategoryDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            daoHelperCategory.initDao(categoryDao);
            int categoryId = categoryDao.findCategoryIdByName(category);
            if (LotValidator.checkLot(title, localDate)) {
                daoHelperLot.initDao(lotDao);
                lotDao.editLot(lotId, categoryId, title, price, image, availability, availableDate);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelperCategory.release();
            daoHelperLot.release();
        }

    }

    /**
     * Deleting lot from database using lot ID.
     *
     * @param lotId ID lot which should be deleted
     * @return
     * @throws ServiceException thrown in case lot have confirmed bets and/or
     *                          orders where made for this lot.
     */
    @Override
    public void deleteLot(long lotId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            lotDao.deleteLot(lotId);
        } catch (DAOException e) {
            throw new ServiceException();
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Method is called in case rejecting payment by costumer.
     * Lot returns in list of lots which available for bidding.
     * Cancelled order added into database for further admin investigation.
     * Delete all bets for this lot.
     *
     * @param lot lot which must be returned to list
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
            OrderDao orderDao = new OrderDaoImpl();
            daoHelper.beginTransaction(betDao, lotDao, orderDao);
            betDao.resetBets(lot.getId());
            lotDao.returnLotToBids(lot.getId(), lot.getPrice(), date);
            orderDao.addCancelledOrder(lot.getId(), customerId, currentPrice);
            daoHelper.commit();
            lot.getBets().clear(); // clear bet list

        } catch (DAOException e) {
            daoHelper.rollback();
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            daoHelper.endTransaction();
        }

    }

    /**
     * Check the auction waiting period.
     * If period is greater than 10 days from current date, all results for this lot must be reset.
     * In other case results for lot are still actual.
     *
     * @param lot lot which time need to be checked.
     * @return true - period less than 10 days and result of auction are actual.
     * false - waiting period is over.
     */
    @Override
    public boolean checkWaitingPeriod(Lot lot) {
        LocalDate lotDateAvailable = lot.getDateAvailable();
        LocalDate currentDate = LocalDate.now();
        return currentDate.minusDays(WAITING_PERIOD).isBefore(lotDateAvailable);
    }

    /**
     * Withdraw the lot or put the lot up for bids
     *
     * @param lotId  lot which status should be changed
     * @param status true - put up lot for auction
     *               false - withdraw lot
     * @throws ServiceException
     */
    @Override
    public void changeLotBiddingStatus(long lotId, boolean status) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            lotDao.changeLotBiddingStatus(lotId, status);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException();
        }
    }

    /**
     * Editing lot date before returning lot to bids.
     * Adding 10 days to current date to get a new date to make the lot available for auction bids.
     *
     * @return new date.
     */
    private LocalDate addDaysToDate() {
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(10);
        return date;
    }

}
