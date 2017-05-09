package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.service.impl.CategoryServiceImpl;
import by.buslauski.auction.validator.CategoryValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 10.04.2017.
 */
public class AddCategoryImp implements Command {
    private static final String CATEGORY_NAME = "name";
    private static final String ERROR = "categoryErr";
    private static CategoryService categoryService = new CategoryServiceImpl();

    /**
     * Creating a new category and insert it into database.
     *
     * @param request user's request.
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String categoryName = request.getParameter(CATEGORY_NAME);
        pageResponse.setPage(returnPageWithQuery(request));
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
