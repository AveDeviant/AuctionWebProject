package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.*;
import by.buslauski.auction.dao.impl.*;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.NotificationService;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * @author Mikita Buslauski
 */
public class LotServiceImpl extends AbstractService implements LotService {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static AuctionService auctionService = new AuctionServiceImpl();

    /**
     * Insert a new lot into database.
     * Lot put up for the auction immediately if added by auction administrator and
     * waits for confirmation if added by customer.
     *
     * @param user          customer who add a lot.
     * @param title         lot tittle.
     * @param description   lot description.
     * @param image         path to lot image.
     * @param price         lot price.
     * @param category      lot category.
     * @param availableDate bidding date.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     */
    @Override
    public void addLot(User user, String title, String description,
                       String image, BigDecimal price,
                       String category, String availableDate) throws ServiceException {
        DaoHelper daoHelperCategory = new DaoHelper();
        DaoHelper daoHelperLot = new DaoHelper();
        long userId = user.getUserId();
        boolean availability = false;
        if (Role.ADMIN == user.getRole()) { // put up lot for the auction immediately
            availability = true;
        }
        try {
            CategoryDaoImpl categoryDao = new CategoryDaoImpl();
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelperCategory.initDao(categoryDao);
            daoHelperLot.initDao(lotDao);
            int categoryId = categoryDao.findCategoryIdByName(category);
            lotDao.addLot(userId, categoryId, title, description, image, price, availableDate, availability, price);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
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
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return lots;
    }

    /**
     * Get available lots from database.
     *
     * @return defined {@link ArrayList} object containing  {@link Lot} objects that have access to the auction
     * ({@link Lot#availability} is <tt>true</tt>).
     */
    @Override
    public ArrayList<Lot> getAvailableLots() throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        ArrayList<Lot> availableLots = new ArrayList<>();
        try {
            LotDaoImpl lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            availableLots.addAll(lotDao.findAvailableLots());
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
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelperLot.release();
            daoHelperBet.release();
        }
        return lot;
    }

    /**
     * Find lot which has permission to bids using lot ID.
     * Set bets to this lot.
     *
     * @param lotId lot ID.
     * @return Lot object.
     * @throws ServiceException if a database access error occurs.
     */
    @Override
    public Lot getAvailableLotById(long lotId) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        DaoHelper commentHelper = new DaoHelper();
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
                CommentDao commentDao = new CommentDaoImpl();
                commentHelper.initDao(commentDao);
                daoHelper.initDao(betDao);
                lot.setBets(betDao.getBetsByLotId(lotId)); // setting bets to lot
                lot.setFollowersCount(betDao.countLotFollowers(lotId));
                lot.setComments(commentDao.getCommentsByLotId(lotId));
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
            commentHelper.release();
        }
        return lot;
    }

    /**
     * Get lots from database which bidding time is over.
     * Lot bidding time ends at 00:00 of day that specified in the field {@link Lot#dateAvailable}.
     * If lot have access to the auction find bets to determine the result of the auction.
     * <p>
     * In case auction result is actual ({@link AuctionService#WAITING_PERIOD} doesn't exceeded):
     * Saving the result of the auction in the database and then notifying winner of the auction and trader.
     * </p>
     * <p>
     * In case {@link AuctionService#WAITING_PERIOD} exceeded:
     * Reset bids for this lot.
     * </p>
     *
     * @return lots for which the auction is ended.
     * @see AuctionServiceImpl#resetBids(Lot)
     * @see NotificationServiceImpl#createNotificationAuctionResult(long, long, long)
     * @see LotServiceImpl#checkWaitingPeriod(Lot)
     */
    @Override
    public ArrayList<Lot> getLotsWithOverTiming() throws ServiceException {
        DaoHelper daoHelperLot = new DaoHelper();
        DaoHelper daoHelperBet = new DaoHelper();
        NotificationService notificationService = new NotificationServiceImpl();
        ArrayList<Lot> lots = new ArrayList<>();
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelperLot.initDao(lotDao);
            lots.addAll(lotDao.findLotsWithOverTiming());
            if (!lots.isEmpty()) {
                BetDaoImpl betDao = new BetDaoImpl();
                daoHelperBet.initDao(betDao);
                for (Lot lot : lots) {
                    if (lot.getAvailability()) {  // check for lot access to the auction
                        lot.getBets().addAll(betDao.getBetsByLotId(lot.getId())); // adding bets to the current lot.
                    }
                    // if auction waiting period is overdue then reset bids results.
                    if (!lot.getBets().isEmpty() && !checkWaitingPeriod(lot)) {
                        auctionService.resetBids(lot);
                    }
                    // if lot has confirmed bets and time is over then notify trader that he has a purchaser.
                    if (!lot.getBets().isEmpty()) {
                        Bet lastBet = lot.getBets().get(lot.getBets().size() - 1);
                        notificationService.createNotificationAuctionResult(lot.getId(),
                                lastBet.getUserId(), lot.getUserId());
                    }
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
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

    /**
     * Change lot data.
     *
     * @param lotId         ID of lot which data needs to be changed;
     * @param category      new lot category;
     * @param title         new lot title;
     * @param image         new path to lot image;
     * @param price         new lot starting price;
     * @param availability  admission of the lot for trading;
     * @param availableDate date to which the lot is put up for the auction.
     * @throws ServiceException in case DAOException has been thrown (database error occurs).
     */
    @Override
    public void editLot(long lotId, String category, String title, String image, BigDecimal price,
                        boolean availability, String availableDate) throws ServiceException {
        DaoHelper daoHelperCategory = new DaoHelper();
        DaoHelper daoHelperLot = new DaoHelper();
        try {
            CategoryDao categoryDao = new CategoryDaoImpl();
            LotDao lotDao = new LotDaoImpl();
            daoHelperCategory.initDao(categoryDao);
            int categoryId = categoryDao.findCategoryIdByName(category);
            daoHelperLot.initDao(lotDao);
            lotDao.editLot(lotId, categoryId, title, price, image, availability, availableDate);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        } finally {
            daoHelperCategory.release();
            daoHelperLot.release();
        }

    }

    /**
     * Deleting lot from database using lot ID.
     * Check that user who called this operation has a permission for this operation ({@link Role#ADMIN}).
     *
     * @param lotId ID lot which should be deleted.
     * @throws ServiceException thrown in case lot have confirmed bets and/or
     *                          orders where made for this lot or SQLException has been thrown for an connection lost
     *                          (or for an another reason).
     */
    @Override
    public void deleteLot(long lotId, User user) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        if (user.getRole() != Role.ADMIN) {
            return;
        }
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            lotDao.deleteLot(lotId);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException();
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Check the auction waiting period.
     * If period is greater than {@link AuctionService#WAITING_PERIOD} days from current date,
     * all results for this lot must be reset.
     * In other case results for lot are still actual.
     *
     * @param lot lot which time need to be checked.
     * @return <tt>true</tt> - period less than {@link AuctionService#WAITING_PERIOD} days and
     * result of the auction is an actual.
     * <tt>false</tt> - waiting period is over.
     */
    @Override
    public boolean checkWaitingPeriod(Lot lot) {
        LocalDate lotDateAvailable = lot.getDateAvailable();
        LocalDate currentDate = LocalDate.now();
        return currentDate.minusDays(AuctionService.WAITING_PERIOD).isBefore(lotDateAvailable);
    }

    /**
     * Withdraw the lot or put the lot up for bids.
     * Check that this operation called by real lot owner or auction administrator.
     *
     * @param lotId  lot which status should be changed.
     * @param status <tt>true</tt> - put up lot for auction;
     *               <tt>false</tt> - withdraw lot.
     * @param user   user who invokes this operation.
     * @throws ServiceException if a database access error occurs.
     */
    @Override
    public void changeLotBiddingStatus(long lotId, boolean status, User user) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            Lot lot = lotDao.findLotById(lotId);
            if ((user == null) || (lot == null) || (user.getRole() != Role.ADMIN && user.getUserId() != lot.getUserId())) {
                return;
            }
            lotDao.changeLotBiddingStatus(lotId, status);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ServiceException();
        }
    }

    /**
     * Find available lots that were exhibited by a concrete trader.
     *
     * @param traderId trader's ID.
     * @return trader lots.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     */
    @Override
    public ArrayList<Lot> findTraderLots(long traderId) throws ServiceException {
        ArrayList<Lot> traderLots = new ArrayList<>();
        ArrayList<Lot> allAvailableLots = getAvailableLots();
        for (Lot lot : allAvailableLots) {
            if (lot.getUserId() == traderId) {
                traderLots.add(lot);
            }
        }
        return traderLots;
    }

    /**
     * Get customer lots that were approved for auction by administrator.
     *
     * @param userId customer ID.
     * @return customer lots.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs).
     */
    @Override
    public ArrayList<Lot> findApprovedUserLots(long userId) throws ServiceException {
        ArrayList<Lot> lots = new ArrayList<>();
        ArrayList<Lot> allLots = getAllLots();
        for (Lot lot : allLots) {
            if (lot.getUserId() == userId && lot.getAvailability()) {
                lots.add(lot);
            }
        }
        return lots;
    }

    /**
     * Extending lot bidding period by customer which expose the lot for the auction in case lot bidding time is over
     * and lot have empty bet list.
     * Check that operation called by real lot owner.
     *
     * @param lotId  lot ID.
     * @param days   days count ({@link AuctionService#EXTENDING_PERIOD_MIN} or {@link AuctionService#EXTENDING_PERIOD_MAX}).
     * @param userId ID user who invokes this operation.
     * @return <tt>true</tt> - operation passed successfully;
     * <tt>false</tt> otherwise.
     * @throws ServiceException in case DAOException has been thrown
     *                          (database error occurs)
     */
    @Override
    public boolean extendBiddingPeriod(long lotId, int days, long userId) throws ServiceException {
        boolean flag = false;
        Lot lot = getLotById(lotId);
        LocalDate currentDate = LocalDate.now();
        if (lot == null || !lot.getBets().isEmpty() || lot.getDateAvailable().isAfter(currentDate) || lot.getUserId() != userId) {
            return flag;
        } else if (days != AuctionService.EXTENDING_PERIOD_MIN && days != AuctionService.EXTENDING_PERIOD_MAX) {
            return flag;
        }
        LocalDate extendedDate = currentDate.plusDays(days);
        String formattedDate = extendedDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        DaoHelper daoHelper = new DaoHelper();
        try {
            LotDao lotDao = new LotDaoImpl();
            daoHelper.initDao(lotDao);
            lotDao.extendBiddingPeriod(lotId, formattedDate);
            flag = true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e + "Exception during extending auction period.");
        } finally {
            daoHelper.release();
        }
        return flag;
    }


}
