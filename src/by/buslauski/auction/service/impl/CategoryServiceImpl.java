package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.impl.CategoryDaoImpl;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.CategoryService;

import java.util.ArrayList;


/**
 * @author Mikita Buslauski
 */
public class CategoryServiceImpl extends AbstractService implements CategoryService {

    @Override
    public ArrayList<Category> getAllCategories() throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        ArrayList<Category> categories = new ArrayList<>();
        try {
            CategoryDao categoryDao = new CategoryDaoImpl();
            daoHelper.initDao(categoryDao);
            categories.addAll(categoryDao.getAllCategories());
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
        return categories;
    }

    @Override
    public void addCategory(String name) throws ServiceException {
        DaoHelper daoHelper = new DaoHelper();
        try {
            CategoryDao categoryDao = new CategoryDaoImpl();
            daoHelper.initDao(categoryDao);
            categoryDao.addCategory(name);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }


}
