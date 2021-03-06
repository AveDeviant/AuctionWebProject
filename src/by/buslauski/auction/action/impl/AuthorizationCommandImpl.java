package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.*;
import by.buslauski.auction.service.impl.AuctionServiceImpl;
import by.buslauski.auction.service.impl.MessageServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class AuthorizationCommandImpl implements Command {
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static final String USER_BANNED = "banned";
    private static UserService userService = new UserServiceImpl();
    private static MessageService messageService = new MessageServiceImpl();
    private static AuctionService auctionService = new AuctionServiceImpl();


    /**
     * Performing authorization of the user on the site.
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type:
     * {@link ResponseType#REDIRECT} - operation passed successfully.
     * {@link ResponseType#FORWARD} - detecting errors during operation.
     * String page - {@link PageNavigation#INDEX_PAGE} in case operation passed successfully and
     * {@link PageNavigation#AUTHORIZATION_PAGE} with appropriate message otherwise.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        if (!UserValidator.checkLoginPassword(login, password)) {
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.AUTHORIZATION_INCORRECT_INPUT_VALUES);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
            return pageResponse;
        }
        try {
            User user = userService.authorizationChecking(login, password);
            if (user == null) {
                request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.AUTHORIZATION_INCORRECT_INPUT_VALUES);
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
                return pageResponse;
            }
            HttpSession session = request.getSession();
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            user.setUserMessages(messageService.findUserMessages(user.getUserId()));
            if (user.getAccess()) {
                auctionService.setWinner(user); //check for winning bets made by this customer
            } else {
                session.setAttribute(USER_BANNED, ResponseMessage.USER_BANNED);
            }
            session.setAttribute(SessionAttributes.USER, user);
            session.setAttribute(SessionAttributes.PAGE_BROWSER, new PageBrowser());
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}