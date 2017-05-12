package by.buslauski.auction.action.impl.user;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 05.05.2017.
 */
public class ExtendBiddingPeriodImpl implements Command {
    private static final String LOT_ID = "lotId";
    private static final String PERIOD = "period";
    private static final String EXTENDING_ERROR = "extendErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Extending lot bidding period to 7 or 15 days by trader.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - response type:
     * REDIRECT - operation passed successfully and auction date has been changed.
     * FORWARD - operation failed (current lot date doesn't passed or bets were made on this lot.
     * String page - page for response (current page).
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        long lotId = Long.parseLong(request.getParameter(LOT_ID));
        int days = Integer.parseInt(request.getParameter(PERIOD));
        try {
            if (!lotService.extendBiddingPeriod(lotId, days,user.getUserId())) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.setAttribute(EXTENDING_ERROR, ResponseMessage.EXTENDING_PERIOD_ERROR);
                return pageResponse;
            }
            pageResponse.setResponseType(ResponseType.REDIRECT);
            return pageResponse;
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(EXTENDING_ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}
