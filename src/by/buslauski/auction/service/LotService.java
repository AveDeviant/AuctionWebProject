package by.buslauski.auction.service;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.dao.LotDao;
import by.buslauski.auction.dao.OrderDao;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.validator.LotValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.sql.rowset.serial.SerialException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Created by Acer on 16.03.2017.
 */
public class LotService extends AbstractService {
    private static final int WAITING_PERIOD = 10;       // waiting period for registration of the won lot, in days.
    private static Logger LOGGER = LogManager.getLogger();
    private static LotValidator validator = new LotValidator();


    public void addLot(String title, long userId, String description,
                       String image, BigDecimal price, boolean availability,
                       String category, String availableDate) throws ServiceException {
        LocalDate date = LocalDate.parse(availableDate);
        CategoryDao categoryDao = new CategoryDao();
        LotDao lotDao = new LotDao();
        try {
            if (validator.checkLot(title, date)) {
                int categoryId = categoryDao.findCategoryByName(category);
                lotDao.addLot(userId, categoryId, title, description, image, price, availableDate, availability, price);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            categoryDao.returnConnection();
            lotDao.returnConnection();
        }

    }

    public ArrayList<Lot> getAllLots() throws ServiceException {
        LotDao lotDao = new LotDao();
        ArrayList<Lot> lots = null;
        try {
            lots = lotDao.findAllLots();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            lotDao.returnConnection();
        }
        return lots;
    }

    /**
     * @return ArrayList of lots which available for bidding
     */
    public ArrayList<Lot> getAvailableLots() throws ServiceException {
        LotDao lotDao = new LotDao();
        ArrayList<Lot> availableLots = null;
        try {
            availableLots = lotDao.getAvailableLots();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            lotDao.returnConnection();
        }
        return availableLots;
    }


    public Lot getLotById(long lotId) throws ServiceException {
        LotDao lotDao = new LotDao();
        BetDao betDao = new BetDao();
        Lot lot = null;
        try {
            lot = lotDao.getLotById(lotId);
            if (lot != null) {
                lot.setBets(betDao.getBetsByLotId(lotId)); // setting bets to lot
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            lotDao.returnConnection();
            betDao.returnConnection();
        }
        return lot;
    }

    /**
     * @param lotId
     * @return
     * @throws ServiceException
     */
    public Lot getAvailableLotById(long lotId) throws ServiceException {
        BetDao betDao = new BetDao();
        Lot lot = null;
        try {
            ArrayList<Lot> lots = getAvailableLots();
            for (int i = 0; i < lots.size(); i++) {
                if (lots.get(i).getId() == lotId) {
                    lot = lots.get(i);
                }
            }
            if (lot != null) {
                lot.setBets(betDao.getBetsByLotId(lotId)); // setting bets to lot
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            betDao.returnConnection();
        }
        return lot;
    }

    public boolean checkBetValue(Lot lot, BigDecimal bet) {
        BigDecimal price = lot.getCurrentPrice();
        return (bet.compareTo(price) >= 0);
    }

    /**
     * Method checks if the lot still available for bidding.
     * Lot bidding time ends at 00:00:00
     *
     * @param lot checked lot.
     * @return true -  lot available for bidding
     * false - lot bidding time is over or lot was withdrawn from the auction
     */
    public boolean checkActuality(Lot lot) {
        LocalDate lotDate = lot.getDateAvailable();
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(lotDate.atTime(0, 0, 0)) && lot.getAvailability();
    }

    /**
     * @return ArrayList containing lots which bidding time is over.
     */
    public ArrayList<Lot> getLotsWithOverTiming() throws ServiceException {
        ArrayList<Lot> lots = null;
        LotDao lotDao = new LotDao();
        BetDao betDao = new BetDao();
        try {
            lots = lotDao.getLotsOverTiming();
            if (!lots.isEmpty()) {
                for (Lot lot : lots) {
                    lot.getBets().addAll(betDao.getBetsByLotId(lot.getId())); // adding bets to the current lot.
                }
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            lotDao.returnConnection();
            betDao.returnConnection();
        }
        return lots;
    }

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

    public void editLot(long lotId, String category, String title, String image, BigDecimal price,
                        boolean availability, String availableDate) throws ServiceException {
        LocalDate localDate = LocalDate.parse(availableDate);
        CategoryDao categoryDao = new CategoryDao();
        LotDao lotDao = new LotDao();
        try {
            int categoryId = categoryDao.findCategoryByName(category);
            if (validator.checkLot(title, localDate)) {
                lotDao.editLot(lotId, categoryId, title, price, image, availability, availableDate);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            categoryDao.returnConnection();
            lotDao.returnConnection();
        }

    }

    public boolean deleteLot(long lotId) throws ServiceException {
        LotDao lotDao = new LotDao();
        boolean status = false;
        try {
            status = lotDao.deleteLot(lotId);
        } catch (DAOException e) {
            throw new ServiceException();
        } finally {
            lotDao.returnConnection();
        }
        return status;
    }

    /**
     * Method is called in case rejecting payment by costumer.
     * Lot returns in list of lots which available for bidding.
     * Cancelled order added into database for further admin investigation.
     * Delete all bets for this lot.
     *
     * @param lot lot which must be returned to list
     */
    public void resetBids(Lot lot) throws ServiceException {
        LocalDate date = null;
        Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
        BigDecimal currentPrice = lot.getCurrentPrice();
        long customerId = lastBet.getUserId();
        date = addDaysToDate();
        ProxyConnection connection = null;
        try {
            connection = pool.takeConnectionFromPool();
            connection.setAutoCommit(false);
            BetDao betDao = new BetDao(connection);
            betDao.resetBets(lot.getId());
            LotDao lotDao = new LotDao(connection);
            lotDao.returnLotToBids(lot.getId(), lot.getPrice(), date);
            OrderDao orderDao = new OrderDao(connection);
            orderDao.addCancelledOrder(lot.getId(), customerId, currentPrice);
            connection.commit();
            ArrayList<Bet> lotBets = lot.getBets();
            lotBets.clear();            // clear bet list
            lot.setBets(lotBets);       // set empty bet list to lot
        } catch (DAOException | SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            returnConnection(connection);
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
    public boolean checkWaitingPeriod(Lot lot) {
        LocalDate lotDateAvailable = lot.getDateAvailable();
        LocalDate currentDate = LocalDate.now();
        return currentDate.minusDays(WAITING_PERIOD).isBefore(lotDateAvailable);
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
