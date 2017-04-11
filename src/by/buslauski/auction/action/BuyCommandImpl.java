package by.buslauski.auction.action;

import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.BankService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Acer on 25.03.2017.
 */
public class BuyCommandImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static final String NAME_PARAM = "name";
    private static final String CITY_PARAM = "city";
    private static final String ADDRESS_PARAM = "address";
    private static final String PHONE_PARAM = "phone";
    private static BankService bankService = new BankService();
    private static UserService userService = new UserService();

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        String realName = request.getParameter(NAME_PARAM);
        String city = request.getParameter(CITY_PARAM);
        String address = request.getParameter(ADDRESS_PARAM);
        String phone = request.getParameter(PHONE_PARAM);
        Bet winningBet = user.getWinningBets().get(0);
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
        if (!user.getAccess()) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.USER_BANNED);
            return pageResponse;
        }
        try {
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
