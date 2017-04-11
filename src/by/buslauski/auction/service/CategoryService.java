package by.buslauski.auction.service;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;

import java.util.ArrayList;


/**
 * Created by Acer on 28.03.2017.
 */
public class CategoryService extends AbstractService {

    public ArrayList<Category> getAllCategories() throws ServiceException {
        CategoryDao categoryDao = new CategoryDao();
        ArrayList<Category> categories = null;
        try {
            categories = categoryDao.getAllCategories();
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            categoryDao.returnConnection();
        }
        return categories;
    }

    public void addCategory(String name) throws ServiceException {
        CategoryDao categoryDao = new CategoryDao();
        try {
            categoryDao.addCategory(name);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }


}
