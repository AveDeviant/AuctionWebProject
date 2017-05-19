package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.dao.exception.DAOException;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface CategoryDao {

    void addCategory(String name) throws DAOException;

    ArrayList<Category> getAllCategories() throws DAOException;

    int findCategoryIdByName(String name) throws DAOException;
}
