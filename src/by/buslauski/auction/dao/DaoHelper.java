package by.buslauski.auction.dao;

import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.impl.AbstractDao;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by Acer on 12.04.2017.
 */
public class DaoHelper {
    private Logger LOGGER = LogManager.getLogger();
    private ProxyConnection connection = ConnectionPool.getInstance().takeConnectionFromPool();

    public void beginTransaction(Object... daos) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
        }
        for (Object dao : daos) {
            ((AbstractDao) dao).setConnection(connection);
        }
    }

    public void endTransaction() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
        }
        ConnectionPool.getInstance().returnConnectionToPool(connection);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    public void initDao(Object dao) {
        ((AbstractDao) dao).setConnection(connection);
    }

    public void release() {
        ConnectionPool.getInstance().returnConnectionToPool(connection);
    }
}