package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
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
    private static BankService bankService = new BankServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Bet operation. Updating lot current price and insert the bet into database.
     * <p>
     * Checked situations:
     * The user isn't authorized on the site;
     * The user hasn't registered his bank card;
     * The user have been banned during his session on the site;
     * The lot was withdrawn from the auction during user's session;
     * The user tries to bet on his own lot;
     * Invalid price entered;
     * The entered price is less than or equals to current lot price;
     * Insufficient funds to confirm bet;
     * Exception during operation;
     *
     * @param request user's request
     * @return
     * @see BetValidator
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionAttributes.USER);
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
                Lot lot = lotService.getAvailableLotById(lotId); //updating lot info
                if (lot != null) {
                    if (lot.getUserId() == user.getUserId()) {
                        request.setAttribute(BET_ERROR, ResponseMessage.BET_ON_OWN_LOT);
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
                    if (!betService.addBet(userId, lotId, newPrice)) {
                        request.setAttribute(BET_ERROR, ResponseMessage.BET_SIZE_ERROR);
                        pageResponse.setResponseType(ResponseType.FORWARD);
                        return pageResponse;
                    }
                    user.setBets(betService.getUserBets(user));
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                    return pageResponse;
                } else {
                    pageResponse.setPage(definePathToAccessDeniedPage(request));
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                }
            } else {
                request.setAttribute(BET_ERROR, ResponseMessage.ACCESS_DENIED);
                pageResponse.setResponseType(ResponseType.FORWARD);
                session.setAttribute(SessionAttributes.USER, user);
            }

        } catch (ServiceException e) {
            request.setAttribute(BET_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
