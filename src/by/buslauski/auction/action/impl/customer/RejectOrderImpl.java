package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.AuctionServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class RejectOrderImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static LotService lotService = new LotServiceImpl();
    private static AuctionService auctionService = new AuctionServiceImpl();

    /**
     * Rejecting deal by customer.
     * Reset auction results for lot (deleting all bets made on this lot).
     * Return lot to bids.
     * Delete first winning bet ({@link Bet} from customer's winning list ({@link User#winningBets}).
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - {@link ResponseType#REDIRECT} in case operation passed successfully and the auction results
     * were cancelled and {@link ResponseType#FORWARD} in other case;
     * String page - page for response - {@link PageNavigation#INDEX_PAGE} if operation passed successfully and
     * current page with appropriate message in other case.
     * @see Command#returnPageWithQuery(HttpServletRequest)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (user.getWinningBets().isEmpty()) {
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            return pageResponse;
        }
        Bet bet = user.getWinningBets().get(0);
        pageResponse.setPage(Command.returnPageWithQuery(request));
        try {
            Lot lot = lotService.getLotById(bet.getLotId());
            auctionService.resetBids(lot);
            user.getWinningBets().remove(bet);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        } catch (ServiceException e) {
            request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
