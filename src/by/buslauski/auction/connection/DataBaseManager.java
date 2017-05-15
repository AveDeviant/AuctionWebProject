package by.buslauski.auction.connection;

import java.util.ResourceBundle;

/**
 * @author Mikita Buslauski
 */
class DataBaseManager {
    private static final String PATH = "properties.databaseConfig";
    private static DataBaseManager instance = new DataBaseManager();
    private ResourceBundle bundle = ResourceBundle.getBundle(PATH);

    private DataBaseManager() {
    }

    static DataBaseManager getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return bundle.getString(key);
    }
}
