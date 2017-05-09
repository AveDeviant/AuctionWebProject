package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 05.04.2017.
 */
public class DeleteLotImpl implements Command {
    private static final String LOT_ID = "lotId";
    private static final String DELETE_ERROR = "editErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Delete lot from database.
     * Displaying appropriate message in case any bet or
     * order have already been made for this lot.
     *
     * @param request user's request
     * @return <code>PageResponse</code> object containing two fields:
     * ResponseType - response type:
     * REDIRECT - operation passed successfully;
     * FORWARD - exception during operation.
     * String page - current page.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        try {
            long lotId = Long.parseLong(request.getParameter(LOT_ID));
            lotService.deleteLot(lotId);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            request.setAttribute(DELETE_ERROR, ResponseMessage.DELETE_LOT_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
