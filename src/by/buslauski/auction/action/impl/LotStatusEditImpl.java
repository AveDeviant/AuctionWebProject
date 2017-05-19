package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.util.NumberParser;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class LotStatusEditImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String STATUS = "status";
    private static final String EDIT_ERROR = "editErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Changing lot bidding status. Withdraw or accept lot for bids.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} if operation passed successfully or
     * {@link ResponseType#FORWARD} in case operation failed.
     * String page - current page.
     * @see Command#returnPageWithQuery(HttpServletRequest)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        pageResponse.setPage(Command.returnPageWithQuery(request));
        long lotId = NumberParser.parse(request.getParameter(LOT_ID));
        boolean status = Boolean.valueOf(request.getParameter(STATUS));
        try {
            lotService.changeLotBiddingStatus(lotId, status, currentUser);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(EDIT_ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}
