package by.buslauski.auction.action.impl.user;

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
 * Created by Acer on 04.05.2017.
 */
public class GetUserLotsImpl implements Command {
    private static final String USER_LOTS = "lots";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Handling client request.
     *
     * @param request user's request
     * @return An object containing two fields:
     * ResponseType - response type (forward or redirect).
     * String page - page for response.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.USER_LOTS);
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (currentUser==null){
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(definePathToAccessDeniedPage(request));
            return pageResponse;
        }
        try {
            ArrayList<Lot> lots = lotService.findApprovedUserLots(currentUser.getUserId());
            request.setAttribute(USER_LOTS, lots);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
