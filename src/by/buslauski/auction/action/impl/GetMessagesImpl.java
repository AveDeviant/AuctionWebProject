package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.impl.MessageServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 07.04.2017.
 */
public class GetMessagesImpl implements Command {
    private static MessageService messageService = new MessageServiceImpl();

    /**
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        try {
            ArrayList<UserMessage> messages = messageService.findMessages(user.getUserId());
            user.setUserMessages(messages);
            messageService.changeMessageStatus(user.getUserId());
            user.setUnreadMessages(false);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return null;
    }
}
