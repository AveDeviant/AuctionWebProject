package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.util.NumberParser;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class GetLotsByTraderImpl implements Command {
    private static final String TRADER_ID = "traderId";
    private static final String TRADER_LOTS = "traderLots";
    private static LotService lotService = new LotServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Get lot which exposed for the auction by concrete trader.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type: {@link ResponseType#FORWARD}
     * String page - page for response {@link PageNavigation#TRADER_LOTS}.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.TRADER_LOTS);
        long traderId = NumberParser.parse(request.getParameter(TRADER_ID));
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
