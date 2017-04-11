package by.buslauski.auction.action;

import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Acer on 13.03.2017.
 */
public class
RegistrationCommandImpl implements Command {
    private static final String REGISTRATION_ERROR_ATTR = "registrationError";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String REPEAT_PASSWORD = "password2";
    private static final String MAIL = "mail";
    private static UserService userService = new UserService();

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = null;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String passwordRepeat = request.getParameter(REPEAT_PASSWORD);
        String email = request.getParameter(MAIL);
        if (!password.equals(passwordRepeat)) {
            request.setAttribute(REGISTRATION_ERROR_ATTR, ResponseMessage.PASSWORD_NOT_EQUAL);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.REGISTRATION_PAGE);
            return pageResponse;
        }
        try {
            user = userService.registerUser(login, password, email);
            if (user == null) {
                request.setAttribute(REGISTRATION_ERROR_ATTR, ResponseMessage.NOT_UNIQUE_NAME_EMAIL);
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.REGISTRATION_PAGE);
                return pageResponse;
            }
            HttpSession session = request.getSession();
            session.setAttribute(RequestAttributes.USER, user);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}