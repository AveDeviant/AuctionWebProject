package by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface LotService {
    int WAITING_PERIOD = 10;  // waiting period for registration of the won lot, in days.
    int EXTENDING_PERIOD_MIN = 7; // extended bidding period,bottom line, in days.
    int EXTENDING_PERIOD_MAX = 15; // extended bidding period, upper bound, in days.

    void addLot(User user, String title, String description,
                String image, BigDecimal price,
                String category, String availableDate) throws ServiceException;

    ArrayList<Lot> getAllLots() throws ServiceException;

    ArrayList<Lot> getAvailableLots() throws ServiceException;

    Lot getLotById(long lotId) throws ServiceException;

    Lot getAvailableLotById(long lotId) throws ServiceException;

    ArrayList<Lot> getLotsWithOverTiming() throws ServiceException;

    ArrayList<Lot> findLotsByCategory(String category) throws ServiceException;

    void editLot(long lotId, String category, String title, String image, BigDecimal price,
                 boolean availability, String availableDate) throws ServiceException;

    void deleteLot(long lotId, User user) throws ServiceException;

    void resetBids(Lot lot) throws ServiceException;

    boolean checkWaitingPeriod(Lot lot);

    void changeLotBiddingStatus(long lotId, boolean status, User user) throws ServiceException;

    ArrayList<Lot> findTraderLots(long traderId) throws ServiceException;

    ArrayList<Lot> findApprovedUserLots(long userId) throws ServiceException;

    boolean extendBiddingPeriod(long lotId, int days, long userId) throws ServiceException;
}
