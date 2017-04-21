package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.DAOException;
import org.apache.logging.log4j.Level;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 17.03.2017.
 */
public class CategoryDaoImpl extends AbstractDao implements CategoryDao{
    private static final String SQL_SELECT_ID = "SELECT id_category FROM category WHERE name=?";
    private static final String SQL_SELECT_ALL_CATEGORIES = "SELECT id_category, name FROM category";
    private static final String SQL_INSERT_CATEGORY ="INSERT INTO category VALUES(NULL,?)";


    @Override
    public int findCategoryIdByName(String name) throws DAOException {
        int categoryId = 0;
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryId = resultSet.getInt("id_category");
                return categoryId;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }
        return categoryId;
    }

    @Override
    public ArrayList<Category> getAllCategories() throws DAOException {
        ArrayList<Category> categories = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_CATEGORIES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(new Category(resultSet.getInt("id_category"),
                        resultSet.getString("name")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }
        return categories;
    }

    @Override
    public void addCategory(String name) throws DAOException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CATEGORY)) {
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR,e);
            throw new DAOException(e);
        }

    }
}
