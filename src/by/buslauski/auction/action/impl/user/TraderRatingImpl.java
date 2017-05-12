package by.buslauski.auction.action.impl.user;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 26.04.2017.
 */
public class TraderRatingImpl implements Command {
    private static final String RATING = "rating";
    private static final String TRADER_ID = "traderId";
    private static final String ERROR = "error";
    private static UserService userService = new UserServiceImpl();

    /**
     * Updating trader rating by customer who confirm the deal.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - {@link ResponseType#REDIRECT} in case operation passed successfully
     * and {@link ResponseType#FORWARD} in other case;
     * String page - page for response (current page).
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        int rating = Integer.parseInt(request.getParameter(RATING));
        User customer = (User) request.getSession().getAttribute(SessionAttributes.USER);
        long traderId = Long.parseLong(request.getParameter(TRADER_ID));
        try {
            userService.updateTraderRating(traderId, customer.getUserId(), rating);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ERROR, ResponseMessage.OPERATION_ERROR);
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
