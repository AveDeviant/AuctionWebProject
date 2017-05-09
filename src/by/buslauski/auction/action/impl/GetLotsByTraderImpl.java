package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 02.05.2017.
 */
public class GetLotsByTraderImpl implements Command {
    private static final String TRADER_ID = "traderId";
    private static final String TRADER_LOTS = "traderLots";
    private static LotService lotService = new LotServiceImpl();
    private static UserService userService = new UserServiceImpl();


    /**
     * Handling client request.
     *
     * @param request user's request.
     * @return <code>PageResponse</code> object containing two fields:
     * ResponseType - response type FORWARD.
     * String page - page for response "/jsp/trader_lots.jsp".
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.TRADER_LOTS);
        long traderId = Long.parseLong(request.getParameter(TRADER_ID));
        try {
            ArrayList<Lot> traderLots = lotService.findTraderLots(traderId);
            User trader = userService.findUserById(traderId);
            if (trader == null) {
                pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
                return pageResponse;
            }
            userService.setTraderRating(trader);
            request.setAttribute(SessionAttributes.TRADER, trader);
            request.setAttribute(TRADER_LOTS, traderLots);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
