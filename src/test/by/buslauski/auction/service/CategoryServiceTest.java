package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.service.impl.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class CategoryServiceTest {

    /**
     * Note that database already stores a lot category with title "other".
     */
    @Test(expected = ServiceException.class)
    public void addCategoryTest() throws ServiceException {
        CategoryService categoryService = new CategoryServiceImpl();
        categoryService.addCategory("other");
    }

    @Test
    public void getCategoriesTest() throws ServiceException {
        CategoryService categoryService = new CategoryServiceImpl();
        ArrayList<Category> categories = categoryService.getAllCategories();
        Assert.assertTrue(categories.size() > 0);
    }
}
