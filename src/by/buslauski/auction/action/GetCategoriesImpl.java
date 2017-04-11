package by.buslauski.auction.action;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.CategoryService;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 02.04.2017.
 */
public class GetCategoriesImpl implements Command {
    private static final String CATEGORIES = "categories";
    private static CategoryService categoryService = new CategoryService();


    /**
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        try {
            ArrayList<Category> categories = categoryService.getAllCategories();
            request.setAttribute(CATEGORIES, categories);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e + " Error during getting categories from database.");
        }
        PageResponse pageResponse = null;
        return pageResponse;
    }
}
