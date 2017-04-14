package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
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
public class BuyCommandImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static final String ERROR_ATTR_MAIN_PAGE = "err";
    private static final String NAME_PARAM = "name";
    private static final String CITY_PARAM = "city";
    private static final String ADDRESS_PARAM = "address";
    private static final String PHONE_PARAM = "phone";
    private static BankServiceImpl bankService = new BankServiceImpl();
    private static UserService userService = new UserServiceImpl();
    private static LotService lotService = new LotServiceImpl();

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        String realName = request.getParameter(NAME_PARAM);
        String city = request.getParameter(CITY_PARAM);
        String address = request.getParameter(ADDRESS_PARAM);
        String phone = request.getParameter(PHONE_PARAM);
        Bet winningBet = user.getWinningBets().get(0);
        pageResponse.setPage(returnPageWithQuery(request));
        if (!user.getAccess()) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.USER_BANNED);
            return pageResponse;
        }
        try {
            // The winnings are not processed within 10 days.
            if (!lotService.checkWaitingPeriod(lotService.getLotById(winningBet.getLotId()))) {
                user.getWinningBets().remove(winningBet);
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.INDEX_PAGE);
                request.setAttribute(ERROR_ATTR_MAIN_PAGE, ResponseMessage.ACCESS_DENIED);
                return pageResponse;
            }
            // Current customer's balance less than lot price.
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
                pageResponse.setResponseType(ResponseType.REDIRECT);
                pageResponse.setPage(PageNavigation.INDEX_PAGE);
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
