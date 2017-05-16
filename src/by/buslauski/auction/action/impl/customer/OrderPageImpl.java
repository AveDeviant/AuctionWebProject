package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class OrderPageImpl implements Command {
    private static final String LOT_ATTRIBUTE = "lot";
    private static UserService userService = new UserServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    /**
     * Showing order page with first lot in customer's winning list {@link User#winningBets}.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - {@link ResponseType#FORWARD}.
     * String page - "/jsp/order.jsp"
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (!user.getWinningBets().isEmpty()) {
            Bet bet = user.getWinningBets().get(0);
            try {
                Lot lot = lotService.getLotById(bet.getLotId());
                User trader = userService.findUserById(lot.getUserId());
                request.setAttribute(LOT_ATTRIBUTE, lot);
                request.setAttribute(SessionAttributes.TRADER, trader);
            } catch (ServiceException e) {
                LOGGER.log(Level.ERROR, e);
            }
        }
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.ORDER_PAGE);
        return pageResponse;
    }
}
