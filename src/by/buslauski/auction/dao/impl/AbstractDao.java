
package by.buslauski.auction.dao.impl;

import by.buslauski.auction.connection.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Mikita Buslauski
 */
public abstract class AbstractDao {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected ProxyConnection connection;

    public void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }
}
