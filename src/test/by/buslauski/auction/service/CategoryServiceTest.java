package test.by.buslauski.auction.service;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.service.impl.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class CategoryServiceTest {
    private static CategoryService categoryService;

    @BeforeClass
    public static void init() {
        categoryService = new CategoryServiceImpl();
    }

    /**
     * Note that database already stores a lot category with title "other".
     */
    @Test(expected = ServiceException.class)
    public void addCategoryTest() throws ServiceException {
        categoryService.addCategory("other");
    }

    @Test
    public void getCategoriesTest() throws ServiceException {
        ArrayList<Category> categories = categoryService.getAllCategories();
        Assert.assertTrue(categories.size() > 0);
    }

    @Test
    public void categoryExistsUnknownCategory() throws ServiceException {
        boolean unknown = categoryService.categoryExists("Guess, who's back?");
        Assert.assertFalse(unknown);
    }

    @Test
    public void categoryExistsTest() throws ServiceException {
        boolean automobiles = categoryService.categoryExists("automobiles");
        Assert.assertTrue(automobiles);
    }
}
