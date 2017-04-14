
package by.buslauski.auction.dao.impl;

import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.BankAccountDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by Acer on 28.02.2017.
 */
public abstract class AbstractDao{
    private static ConnectionPool pool = ConnectionPool.getInstance();
    static final Logger LOGGER = LogManager.getLogger();
    protected ProxyConnection connection;


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

    public void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }
}
