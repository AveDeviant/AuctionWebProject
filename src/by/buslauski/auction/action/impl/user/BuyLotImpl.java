package by.buslauski.auction.action.impl.user;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.impl.BankServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Acer on 25.03.2017.
 */
public class BuyLotImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static final String NAME_PARAM = "name";
    private static final String CITY_PARAM = "city";
    private static final String ADDRESS_PARAM = "address";
    private static final String PHONE_PARAM = "phone";
    private static BankServiceImpl bankService = new BankServiceImpl();
    private static UserService userService = new UserServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    /**
     * Updating customer's personal information.
     * Payment transaction from customer to auction or creation notification for trader about auction results.
     * Deleting first winning bet from customer's bet list.
     * Withdraw lot from bids.
     * <p>
     * Checked situations:
     * Customer unable to register his order;
     * Customer's current balance is less than lot price;
     * Customer exceeded auction waiting period (10 days) or lot was blocked;
     * Invalid customer's personal information;
     * Exception during payment transaction.
     *
     * @param request user's request
     * @return A <code>PageResponse</code> object containing two fields:
     * ResponseType - response type:
     * REDIRECT - operation passed successfully;
     * FORWARD - operation failed.
     * String page - page for response "/jsp/success.jsp" if operation passed successfully
     * ana current page with message if operation failed.
     * @see UserValidator
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        String realName = request.getParameter(NAME_PARAM);
        String city = request.getParameter(CITY_PARAM);
        String address = request.getParameter(ADDRESS_PARAM);
        String phone = request.getParameter(PHONE_PARAM);
        Bet winningBet = user.getWinningBets().get(0);
        pageResponse.setPage(returnPageWithQuery(request));
        try {
            user = userService.findUserById(user.getUserId()); // updating user info.
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
            // Current customer's balance less that lot price.
            if (!bankService.checkIsEnoughBalance(user.getUserId(), winningBet.getBet())) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.ORDER_BANK_BALANCE_ERROR);
                return pageResponse;
            }
            if (UserValidator.checkUserInfo(realName, city, address, phone)) {
                userService.updateUserInfo(user.getUserId(), realName, city, address, phone);
                if (!bankService.doPayment(winningBet)) {
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.ORDER_TRANSACTION_EXCEPTION);
                    return pageResponse;
                }
                user.getWinningBets().remove(winningBet);
                request.getSession().setAttribute(SessionAttributes.USER, user);  // update user in the session.
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
