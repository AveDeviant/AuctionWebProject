package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.util.NumberParser;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class ShowLotImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String LOT = "lot";
    private static LotService lotService = new LotServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Showing lot and trader info.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType  - response type {@link ResponseType#FORWARD}
     * String page - "/jsp/lot.jsp"  in case lot with entered ID available for the auction;
     * "/jsp/404.jsp" in other case.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        long lotId = NumberParser.parse(request.getParameter(LOT_ID));
        try {
            Lot lot = lotService.getAvailableLotById(lotId);
            if (lot == null) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
                return pageResponse;
            }
            User trader = userService.findTrader(lot.getId());
            userService.setTraderRating(trader);
            request.setAttribute(SessionAttributes.TRADER, trader);
            request.setAttribute(LOT, lot);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.LOT_PAGE);
        } catch (ServiceException e) {
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
