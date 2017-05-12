package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.service.impl.CategoryServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 02.04.2017.
 */
public class GetCategoriesImpl implements Command {
    private static final String CATEGORIES = "categories";
    private static CategoryService categoryService = new CategoryServiceImpl();


    /**
     * Return all lot categories from database.
     *
     * @param request client request to get parameters to work with.
     * @return null
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        try {
            ArrayList<Category> categories = categoryService.getAllCategories();
            request.setAttribute(CATEGORIES, categories);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e + " Error during getting categories from database.");
        }
        return null;
    }
}
