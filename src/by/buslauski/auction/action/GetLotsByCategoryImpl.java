package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 29.03.2017.
 */
public class GetLotsByCategoryImpl implements Command {
    private static final String CATEGORY = "categoryValue";
    private static final String LOTS_BY_CATEGORY = "lotsByCategory";
    private static final String EMPTY_LIST = "emptyList";
    private static final String ERROR = "err";
    private static LotService lotService = new LotServiceImpl();


    /**
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String category = request.getParameter(CATEGORY);
        try {
            ArrayList<Lot> lotsByCategory = lotService.findLotsByCategory(category);
            if (lotsByCategory.isEmpty()) {
                request.setAttribute(EMPTY_LIST, ResponseMessage.EMPTY_LOT_LIST);
            }
            request.setAttribute(LOTS_BY_CATEGORY, lotsByCategory);
            request.setAttribute(CATEGORY, category);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.MAIN_PAGE);
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}
