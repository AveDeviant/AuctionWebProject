package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.BankService;
import by.buslauski.auction.service.BetService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;

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
    private static LotService lotService = new LotService();
    private static BetService betService = new BetService();
    private static BankService bankService = new BankService();
    private static UserService userService = new UserService();


    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        HttpSession session = request.getSession();
        BigDecimal newPrice = new BigDecimal(request.getParameter(PRICE_PARAM));
        User user = (User) session.getAttribute(RequestAttributes.USER);
        long lotId = Long.valueOf(request.getParameter(LOT_ID_PARAM));
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
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
            if (user.getAccess()) {
                Lot lot = lotService.getLotById(lotId);// ЙОУ ЙОУ ЙОУ можешь это сунуть в сервис где сравниваешь цены лота(там вызывать дао который
                if (!lotService.checkActuality(lot)) {
                    request.setAttribute(BET_ERROR, ResponseMessage.BET_TIMEOUT);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
                if (!lotService.checkBetValue(lot, newPrice)) {
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
