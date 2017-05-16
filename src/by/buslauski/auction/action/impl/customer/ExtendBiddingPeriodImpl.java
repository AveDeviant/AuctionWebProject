package by.buslauski.auction.action.impl.customer;

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
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class ExtendBiddingPeriodImpl implements Command {
    private static final String LOT_ID = "lotId";
    private static final String PERIOD = "period";
    private static final String EXTENDING_ERROR = "extendErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Extending lot bidding period to {@link LotService#EXTENDING_PERIOD_MIN} or
     * {@link LotService#EXTENDING_PERIOD_MAX} (in days) by trader.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} - operation passed successfully and auction date has been changed or
     * {@link ResponseType#FORWARD} if operation failed (current lot date doesn't passed or bets were made on this lot).
     * String page - page for response (current page).
     * @see Command#returnPageWithQuery(HttpServletRequest)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        long lotId = NumberParser.parse(request.getParameter(LOT_ID));
        int days = (int) NumberParser.parse(request.getParameter(PERIOD));
        try {
            if (!lotService.extendBiddingPeriod(lotId, days, user.getUserId())) {
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
