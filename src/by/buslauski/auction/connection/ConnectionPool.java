package by.buslauski.auction.connection;

import com.mysql.jdbc.Driver;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Acer on 28.02.2017.
 */
public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();
    private static ConnectionPool instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private BlockingQueue<ProxyConnection> connections;
    private static ReentrantLock lock = new ReentrantLock();

    private static final String DB_URL = "db.url";
    private static final String ADMIN = "db.admin";
    private static final String PASSWORD = "db.password";
    private static final String CONNECTION_COUNT = "db.connection.count";

    private String url;
    private String login;
    private String password;
    private int connectionCount;


    private ConnectionPool() {
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        this.url = dataBaseManager.getValue(DB_URL);
        this.login = dataBaseManager.getValue(ADMIN);
        this.password = dataBaseManager.getValue(PASSWORD);
        this.connectionCount = Integer.parseInt(dataBaseManager.getValue(CONNECTION_COUNT));
        this.connections = new ArrayBlockingQueue<>(connectionCount);
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e + " Driver can't be registered");
            throw new RuntimeException(e);
        }
        for (int i = 0; i < connectionCount; i++) {
            ProxyConnection connection = createConnection();
            if (connection != null) {
                connections.add(createConnection());
            }
        }
        if (connections.isEmpty()) {
            LOGGER.fatal("Problems with connection pool has been detected.");
            throw new RuntimeException();
        }
    }

    private ProxyConnection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);// log error
        }
        return new ProxyConnection(connection);
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    isCreated.getAndSet(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ProxyConnection takeConnectionFromPool() {
        ProxyConnection connection = null;
        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e + " Can't take connection from pool.");
        }
        return connection;
    }

    public void returnConnectionToPool(ProxyConnection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e + " Interrupted exception: can't return connection to pool.");
        }
    }

    public void closeConnections() {
        while (!connections.isEmpty()) {
            try {
                takeConnectionFromPool().closeConnection();
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, e + " Can't close connection");
            }
        }
        try {
            Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = (Driver) drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
            LOGGER.info("Drivers has been deregistered");
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e + "Problem during deregister drivers");
            e.printStackTrace();
        }
        LOGGER.info("All connections has been closed.");
    }

}

