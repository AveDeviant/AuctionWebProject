package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.dao.impl.CategoryDaoImpl;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.service.CategoryService;

import java.util.ArrayList;


/**
 * Created by Acer on 28.03.2017.
 */
public class CategoryServiceImpl extends AbstractService implements CategoryService {

    @Override
    public ArrayList<Category> getAllCategories() throws ServiceException {
        CategoryDaoImpl categoryDao = new CategoryDaoImpl();
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

    @Override
    public void addCategory(String name) throws ServiceException {
        CategoryDao categoryDao = new CategoryDaoImpl();
        try {
            categoryDao.addCategory(name);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }


}