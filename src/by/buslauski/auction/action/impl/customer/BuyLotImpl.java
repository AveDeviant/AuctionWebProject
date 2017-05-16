package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.impl.AuctionServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Mikita Buslauski
 */
public class BuyLotImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static final String NAME_PARAM = "name";
    private static final String CITY_PARAM = "city";
    private static final String ADDRESS_PARAM = "address";
    private static final String PHONE_PARAM = "phone";
    private static AuctionService auctionService = new AuctionServiceImpl();
    private static UserService userService = new UserServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    /**
     * Updating customer's personal information.
     * Creating notification for trader about auction results.
     * Deleting first winning bet from customer's bet list.
     * Withdraw lot from bids.
     * <p>
     * Checked situations:
     * Customer unable to register his order;
     * Customer exceeded auction waiting period (10 days) or lot was blocked;
     * Invalid customer's personal information;
     * Exception during operation.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} - operation passed successfully or
     * {@link ResponseType#FORWARD} if operation failed.
     * String page - page for response "/jsp/success.jsp" if operation passed successfully
     * ana current page with message if operation failed.
     * @see Command#returnPageWithQuery(HttpServletRequest)
     * @see Command#definePathToSuccessPage(HttpServletRequest)
     * @see Command#definePathToAccessDeniedPage(HttpServletRequest)
     * @see UserValidator
     * @see PageBrowser
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        String realName = request.getParameter(NAME_PARAM);
        if (user.getWinningBets().isEmpty() || realName.isEmpty()) {
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            return pageResponse;
        }
        String city = request.getParameter(CITY_PARAM);
        String address = request.getParameter(ADDRESS_PARAM);
        String phone = request.getParameter(PHONE_PARAM);
        Bet winningBet = user.getWinningBets().get(0);
        pageResponse.setPage(returnPageWithQuery(request));
        try {
            user = userService.findUserById(user.getUserId()); // updating customer info.
            if (!user.getAccess()) {
                pageResponse.setResponseType(ResponseType.REDIRECT);
                pageResponse.setPage(definePathToAccessDeniedPage(request));
                return pageResponse;
            }
            // The winnings are not processed within 10 days or lot was blocked (withdrawn from the auction).
            Lot lot = lotService.getLotById(winningBet.getLotId());
            if (!lotService.checkWaitingPeriod(lot) || !lot.getAvailability()) {
                user.getWinningBets().remove(winningBet);
                pageResponse.setResponseType(ResponseType.REDIRECT);
                pageResponse.setPage(definePathToAccessDeniedPage(request));
                return pageResponse;
            }
            if (UserValidator.checkUserInfo(realName, city, address, phone)) {
                userService.updateUserInfo(user.getUserId(), realName, city, address, phone);
                if (!auctionService.notifyTrader(winningBet)) {
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.ORDER_TRANSACTION_EXCEPTION);
                    return pageResponse;
                }
                user.getWinningBets().remove(winningBet);
                request.getSession().setAttribute(SessionAttributes.USER, user);  // update customer in the session.
                pageResponse.setResponseType(ResponseType.REDIRECT);
                PageBrowser browser = (PageBrowser) request.getSession().getAttribute(SessionAttributes.PAGE_BROWSER);
                browser.addPageToHistory(returnPageWithQuery(request));
                pageResponse.setPage(definePathToSuccessPage(request));
                return pageResponse;
            } else {
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.INCORRECT_USER_INFORMATION);
            }
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}
