package by.buslauski.auction.action.impl.user;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.impl.BankServiceImpl;
import by.buslauski.auction.validator.BankCardValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 20.03.2017.
 */
public class AddBankCardImpl implements Command {
    private static final String PAYMENT_SYSTEM = "system";
    private static final String CARD_NUMBER = "number";
    private static final String BANK_CARD_ERROR = "bankErr";

    /**
     * Add user's bank card info into database.
     * Setting created bank account to current user.
     * <p>
     * Checked situations:
     * Invalid input;
     * Entered bank card has already been registered by another user;
     * Exception during adding data into database;
     *
     * @param request user's request
     * @return PageResponse object containing two fields:
     * ResponseType - REDIRECT if operation passed successfully and FORWARD in other case;
     * String page - current page.
     * @see BankCardValidator
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionAttributes.USER);
        String paymentSystem = request.getParameter(PAYMENT_SYSTEM);
        String cardNumber = request.getParameter(CARD_NUMBER);
        long userId = user.getUserId();
        BankServiceImpl bankService = new BankServiceImpl();
        BankCard bankCard = null;
        try {
            if (BankCardValidator.checkCardForValidNumber(cardNumber)) {
                bankCard = bankService.addAccount(userId, paymentSystem, cardNumber);
                if (bankCard == null) {
                    request.setAttribute(BANK_CARD_ERROR, ResponseMessage.NOT_UNIQUE_BANK_CARD);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    pageResponse.setPage(PageNavigation.PRIVATE_PAGE);
                    return pageResponse;
                } else {
                    user.setBankCard(bankCard);
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                    return pageResponse;
                }
            } else {
                request.setAttribute(BANK_CARD_ERROR, ResponseMessage.INVALID_CARD_NUMBER);
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.PRIVATE_PAGE);
                return pageResponse;
            }
        } catch (ServiceException e) {
            request.setAttribute(BANK_CARD_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.PRIVATE_PAGE);
        }
        return pageResponse;
    }
}