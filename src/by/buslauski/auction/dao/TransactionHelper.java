package by.buslauski.auction.dao;

import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.impl.AbstractDao;

import java.sql.SQLException;

/**
 * Created by Acer on 12.04.2017.
 */
public class TransactionHelper {
    private ProxyConnection connection = ConnectionPool.getInstance().takeConnectionFromPool();

    public void beginTransaction(AbstractDao...daos){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (AbstractDao dao : daos) {
            dao.setConnection(connection);
        }
    }

    public void endTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
