package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.impl.BankServiceImpl;
import by.buslauski.auction.service.impl.BetServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.BetValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * Created by Acer on 19.03.2017.
 */
public class BetCommandImpl implements Command {
    private static final String PRICE_PARAM = "price";
    private static final String LOT_ID_PARAM = "lotId";
    private static final String BET_ERROR = "betErr";
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static LotService lotService = new LotServiceImpl();
    private static BetService betService = new BetServiceImpl();
    private static BankServiceImpl bankService = new BankServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Bet operation. Updating lot current price and insert the bet into database.
     *
     * @param request
     * @return
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestAttributes.USER);
        long lotId = Long.valueOf(request.getParameter(LOT_ID_PARAM));
        pageResponse.setPage(returnPageWithQuery(request));
        if (user == null) {
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.BET_AUTHORIZATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
            return pageResponse;
        }
        long userId = user.getUserId();
        if (user.getBankCard() == null) {
            request.setAttribute(BET_ERROR, ResponseMessage.BET_BANK_CARD_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.PRIVATE_PAGE);
            return pageResponse;
        }
        try {
            user = userService.findUserById(user.getUserId()); // updating user info
            if (user.getAccess()) {
                Lot lot = lotService.getAvailableLotById(lotId);
                if (lot.getUserId() == user.getUserId()) {
                    request.setAttribute(BET_ERROR, ResponseMessage.BET_ON_OWN_LOT);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                if (!lotService.checkActuality(lot)) {
                    request.setAttribute(BET_ERROR, ResponseMessage.BET_TIMEOUT);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                if (!BetValidator.checkPriceForValid(request.getParameter(PRICE_PARAM))) {
                    request.setAttribute(BET_ERROR, ResponseMessage.INVALID_VALUE);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                BigDecimal newPrice = new BigDecimal(request.getParameter(PRICE_PARAM));
                if (!betService.checkBetValue(lot, newPrice)) {
                    request.setAttribute(BET_ERROR, ResponseMessage.BET_SIZE_ERROR);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                if (!bankService.checkIsEnoughBalance(userId, newPrice)) {
                    request.setAttribute(BET_ERROR, ResponseMessage.BET_BANK_BALANCE_ERROR);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                betService.addBet(userId, lotId, newPrice);
                user.setBets(userService.getUserBets(user));
                pageResponse.setResponseType(ResponseType.REDIRECT);
                return pageResponse;
            } else {
                request.setAttribute(BET_ERROR, ResponseMessage.ACCESS_DENIED);
                pageResponse.setResponseType(ResponseType.FORWARD);
            }
        } catch (ServiceException e) {
            request.setAttribute(BET_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
