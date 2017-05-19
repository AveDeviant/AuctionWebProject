package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.validator.exception.InvalidNumberValueException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.impl.BetServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.util.NumberParser;
import by.buslauski.auction.validator.BetValidator;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @author Buslauski Mikita
 */
public class BetCommandImpl implements Command {
    private static final String PRICE_PARAM = "price";
    private static final String LOT_ID_PARAM = "lotId";
    private static final String BET_ERROR = "betErr";
    private static final String BET_SIZE_ERROR = "betSizeErr";
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static LotService lotService = new LotServiceImpl();
    private static BetService betService = new BetServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Bet operation. Updating lot current price and insert the bet into database.
     * <p>
     * Checked situations:
     * The customer isn't authorized on the site;
     * The customer have been banned during his session on the site;
     * The lot was withdrawn from the auction during customer's session;
     * The customer tries to bet on his own lot;
     * Invalid price entered;
     * The entered price is less than or equals to current lot price;
     * Exception during operation;
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} - operation passed successfully.
     * {@link ResponseType#FORWARD} - detecting errors during operations.
     * String page - current page or {@link PageNavigation#ACCESS_DENIED_PAGE} in case lot became unable for the auction.
     * @see Command#returnPageWithQuery(HttpServletRequest)
     * @see Command#definePathToAccessDeniedPage(HttpServletRequest)
     * @see BetValidator
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        long lotId = NumberParser.parse(request.getParameter(LOT_ID_PARAM));
        pageResponse.setPage(Command.returnPageWithQuery(request));
        pageResponse.setResponseType(ResponseType.FORWARD);
        if (user == null) {
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.BET_AUTHORIZATION_ERROR);
            pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
            return pageResponse;
        }
        long userId = user.getUserId();
        try {
            user = userService.findUserById(user.getUserId()); // updating user info
            if (user.getAccess()) {
                Lot lot = lotService.getAvailableLotById(lotId); //updating lot info
                if (lot != null && user.getRole() != Role.ADMIN) {
                    if (lot.getUserId() == user.getUserId()) {
                        request.setAttribute(BET_ERROR, ResponseMessage.BET_ON_OWN_LOT);
                        return pageResponse;
                    }
                    if (!BetValidator.checkPriceForValid(request.getParameter(PRICE_PARAM))) {
                        request.setAttribute(BET_ERROR, ResponseMessage.INVALID_BET_VALUE);
                        return pageResponse;
                    }
                    BigDecimal newPrice = BetValidator.initPrice(request.getParameter(PRICE_PARAM));
                    if (!betService.checkBetValue(lot, newPrice) || !betService.addBet(userId, lotId, newPrice)) {
                        request.setAttribute(BET_SIZE_ERROR, ResponseMessage.BET_SIZE_ERROR);
                        return pageResponse;
                    }
                    user.setBets(betService.getUserBets(user));
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                    return pageResponse;
                } else {
                    pageResponse.setPage(Command.definePathToAccessDeniedPage(request));
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                }
            } else {
                request.setAttribute(BET_ERROR, ResponseMessage.ACCESS_DENIED);
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.getSession().setAttribute(SessionAttributes.USER, user);
            }

        } catch (ServiceException e) {
            request.setAttribute(BET_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        } catch (InvalidNumberValueException e) {
            request.setAttribute(BET_ERROR, ResponseMessage.INVALID_BET_VALUE);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
