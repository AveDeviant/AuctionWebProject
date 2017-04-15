package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 15.04.2017.
 */
public class LotStatusEditImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String STATUS = "status";
    private static final String EDIT_ERROR = "editErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Changing lot bidding status. Withdraw or accept lot for bids.
     *
     * @param request
     * @return A PageResponse object containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        long lotId = Long.parseLong(request.getParameter(LOT_ID));
        boolean status = Boolean.valueOf(request.getParameter(STATUS));
        try {
            lotService.changeLotBiddingStatus(lotId, status);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(EDIT_ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}
