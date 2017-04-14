package by.buslauski.auction.service.impl;

import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.connection.ProxyConnection;


/**
 * Created by Acer on 03.04.2017.
 */
abstract class AbstractService {
    static ConnectionPool pool = ConnectionPool.getInstance();

    void returnConnection(ProxyConnection connection) {
        if (connection != null) {
            pool.returnConnectionToPool(connection);
        }
    }
}
