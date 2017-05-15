package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.PageBrowser;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.UserValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Mikita Buslauski
 */
public class RegistrationCommandImpl implements Command {
    private static final String REGISTRATION_ERROR_ATTR = "registrationError";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "alias";
    private static final String REPEAT_PASSWORD = "password2";
    private static final String MAIL = "mail";
    private static UserService userService = new UserServiceImpl();

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = null;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String passwordRepeat = request.getParameter(REPEAT_PASSWORD);
        String email = request.getParameter(MAIL);
        String alias = request.getParameter(USERNAME);
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.REGISTRATION_PAGE);
        if (!password.equals(passwordRepeat)) {
            request.setAttribute(REGISTRATION_ERROR_ATTR, ResponseMessage.PASSWORD_NOT_EQUAL);
            return pageResponse;
        }
        if (!UserValidator.checkLoginPasswordAlias(login, password, alias)) {
            request.setAttribute(REGISTRATION_ERROR_ATTR, ResponseMessage.INVALID_VALUE);
            return pageResponse;
        }
        try {
            user = userService.registerUser(login, password, alias, email);
            if (user == null) {
                request.setAttribute(REGISTRATION_ERROR_ATTR, ResponseMessage.NOT_UNIQUE_NAME_EMAIL);
                return pageResponse;
            }
            HttpSession session = request.getSession();
            session.setAttribute(SessionAttributes.USER, user);
            session.setAttribute(SessionAttributes.PAGE_BROWSER, new PageBrowser());
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
