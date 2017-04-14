package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.impl.MessageServiceImpl;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import by.buslauski.auction.validator.MessageValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageCommandImpl implements Command {
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static final String MESSAGE_ERROR = "messageErr";
    private static final String MESSAGE_THEME = "theme";
    private static final String MESSAGE_TEXT = "content";
    private static final String RECIPIENT_ID = "recipientId";
    private static MessageService messageService = new MessageServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * @param request
     * @return
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        if (user == null) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.MESSAGE_ERROR_AUTHORIZATION);
            return pageResponse;
        }
        String theme = request.getParameter(MESSAGE_THEME);
        String text = request.getParameter(MESSAGE_TEXT);
        if (!MessageValidator.checkMessage(text)) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.FAQ_PAGE);
            request.setAttribute(MESSAGE_ERROR, ResponseMessage.MESSAGE_ERROR_INVALID);
            return pageResponse;
        }
        try {
            if (Role.CUSTOMER.getValue().equals(user.getRole().getValue())) {
                User admin = userService.findAdmin();
                messageService.addMessage(theme, text, user, admin);
                pageResponse.setResponseType(ResponseType.REDIRECT);
                pageResponse.setPage(PageNavigation.FAQ_PAGE);
            } else {
                // bug
                long recipientId = Long.parseLong(request.getParameter(RECIPIENT_ID));
                System.out.println(recipientId);
                User customer = userService.findUserById(recipientId);
                messageService.addMessage(theme, text, user, customer);
                pageResponse.setResponseType(ResponseType.REDIRECT);
              pageResponse.setPage(returnPageWithQuery(request));
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            request.setAttribute(MESSAGE_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        }
        return pageResponse;
    }
}
