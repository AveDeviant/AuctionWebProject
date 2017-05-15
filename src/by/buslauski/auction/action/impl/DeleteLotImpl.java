package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.util.NumberParser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
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
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - response type: {@link ResponseType#REDIRECT} - operation passed successfully or
     * {@link ResponseType#FORWARD} - exception during operation.
     * String page - current page.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            long lotId = NumberParser.parse(request.getParameter(LOT_ID));
            lotService.deleteLot(lotId, currentUser);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            request.setAttribute(DELETE_ERROR, ResponseMessage.DELETE_LOT_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
