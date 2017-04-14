package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.impl.MessageServiceImpl;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 27.02.2017.
 */
public class AuthorizationCommandImpl implements Command {
    private static UserService userService = new UserServiceImpl();
    private static MessageService messageService = new MessageServiceImpl();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static final String USER_BANNED = "banned";


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
            session.setAttribute(RequestAttributes.USER, user);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            user.setBets(userService.getUserBets(user));
            user.setUserMessages(messageService.findMessages(user.getUserId()));
            if (messageService.haveUnreadMessages(user.getUserId())){
                user.setUnreadMessages(true);
            }
            if (user.getAccess()) {
                userService.setWinner(user); //check for winning bets made by this customer
            } else {
                session.setAttribute(USER_BANNED, ResponseMessage.USER_BANNED);
            }
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.OPERATION_ERROR);
        }
        return pageResponse;
    }
}