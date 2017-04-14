package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.DAOException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface CategoryDao {

    void addCategory(String name) throws DAOException;

    ArrayList<Category> getAllCategories() throws DAOException;
}
