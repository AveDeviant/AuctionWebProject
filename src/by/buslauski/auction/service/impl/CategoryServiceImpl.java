package by.buslauski.auction.service.impl;

import by.buslauski.auction.dao.CategoryDao;
import by.buslauski.auction.dao.DaoHelper;
import by.buslauski.auction.dao.impl.CategoryDaoImpl;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.CategoryService;
import org.apache.logging.log4j.Level;

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
            LOGGER.log(Level.ERROR, e + " Exception during getting lot categories from database.");
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
            LOGGER.log(Level.ERROR, e + " Exception during adding a new lot category.");
            throw new ServiceException(e);
        } finally {
            daoHelper.release();
        }
    }

    /**
     * Check that entered lot category exists.
     *
     * @param category entered category.
     * @return <tt>true</tt> if entered category stores in database;
     * <tt>false</tt> in other case.
     * @throws ServiceException in case DAOException has been thrown (database error occurs)
     */
    @Override
    public boolean categoryExists(String category) throws ServiceException {
        boolean status = false;
        ArrayList<Category> categories = getAllCategories();
        for (Category entity : categories) {
            if (entity.getValue().equalsIgnoreCase(category)) {
                status = true;
            }
        }
        return status;
    }


}
