package by.buslauski.auction.action;

import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.validator.CategoryValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 10.04.2017.
 */
public class AddCategoryImp implements Command {
    private static final String CATEGORY_NAME = "name";
    private static final String ERROR = "categoryErr";
    private static CategoryService categoryService = new CategoryService();

    /**
     * Insert a new category into database.
     *
     * @param request
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        String categoryName = request.getParameter(CATEGORY_NAME);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            pageResponse.setPage(controller.concat(path.substring(path.lastIndexOf("?"))));
        }
        try {
            if (CategoryValidator.checkCategoryForValid(categoryName)) {
                categoryService.addCategory(categoryName);
                pageResponse.setResponseType(ResponseType.REDIRECT);
            }
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ERROR, ResponseMessage.ADDING_CATEGORY_ERROR);
        }
        return pageResponse;
    }
}
