package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.dao.exception.DAOException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface LotDao {

    void addLot(long userId, int categoryId, String title, String description,
                String image, BigDecimal price, String availableDate,
                boolean availability, BigDecimal currentPrice) throws DAOException;

    ArrayList<Lot> findAllLots() throws DAOException;

    Lot findLotById(long id) throws DAOException;

    ArrayList<Lot> findAvailableLots() throws DAOException;

    ArrayList<Lot> findLotsWithOverTiming() throws DAOException;

    void updateCurrentPrice(long lotId, BigDecimal price) throws DAOException;

    void editLot(long lotId, int categoryId, String title, BigDecimal price, String image, boolean availability,
                 String availableDate) throws DAOException;

    void returnLotToBids(long lotId, BigDecimal price, LocalDate date) throws DAOException;

    void deleteLot(long lotId) throws DAOException;

    void changeLotBiddingStatus(long lotId, boolean status) throws DAOException;

    void extendBiddingPeriod(long lotId, String newDate) throws DAOException;
}
