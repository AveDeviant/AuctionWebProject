package by.buslauski.auction.service;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface LotService {
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
