package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.CategoryService;
import by.buslauski.auction.service.impl.CategoryServiceImpl;
import by.buslauski.auction.validator.CategoryValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class AddCategoryImp implements Command {
    private static final String CATEGORY_NAME = "name";
    private static final String ERROR = "categoryErr";
    private static CategoryService categoryService = new CategoryServiceImpl();

    /**
     * Creating a new lot category and insert it into database.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} if operation passed successfully.
     * {@link ResponseType#FORWARD} in other case.
     * String page - page for response (current page).
     * @see Command#returnPageWithQuery(HttpServletRequest)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String categoryName = request.getParameter(CATEGORY_NAME);
        pageResponse.setPage(Command.returnPageWithQuery(request));
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
