package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.DAOException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface LotDao {

    void addLot(long userId, int categoryId, String title, String description,
                String image, BigDecimal price, String availableDate,
                boolean availability, BigDecimal currentPrice) throws DAOException;

    ArrayList<Lot> findAllLots() throws DAOException;

    Lot findLotById(long id) throws DAOException;
}
