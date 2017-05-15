package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class GetUserLotsImpl implements Command {
    private static final String USER_LOTS = "lots";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Get approved lot which exposed by current customer.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - response type: {@link ResponseType#FORWARD}
     * String page - page for response "/jsp/user_lots.jsp".
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.USER_LOTS);
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            ArrayList<Lot> lots = lotService.findApprovedUserLots(currentUser.getUserId());
            request.setAttribute(USER_LOTS, lots);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
