package by.buslauski.auction.dao;

import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.connection.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by Acer on 28.02.2017.
 */
public abstract class AbstractDao {
    private static ConnectionPool pool = ConnectionPool.getInstance();
    static final Logger LOGGER = LogManager.getLogger();
    ProxyConnection connection;


    AbstractDao() {
        this.connection = pool.takeConnectionFromPool();
    }

    AbstractDao(ProxyConnection connection) {
        this.connection = connection;
    }

    public void setAutoCommitTrue() throws SQLException {
        connection.setAutoCommit(true);
    }

    public void setAutoCommitFalse() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void returnConnection() {
        if (connection != null) {
            pool.returnConnectionToPool(connection);
        }
    }
}
