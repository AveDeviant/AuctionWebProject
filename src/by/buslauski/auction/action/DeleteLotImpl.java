package by.buslauski.auction.action;

import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 05.04.2017.
 */
public class DeleteLotImpl implements Command {
    private static final String LOT_ID = "lotId";
    private static final String DELETE_ERROR = "editErr";
    LotService lotService = new LotService();

    /**
     * Delete lot from database.
     * Displaying appropriate message in case any bet or order have already been mady for his lot.
     *
     * @param request
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String controller = request.getRequestURI();
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
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