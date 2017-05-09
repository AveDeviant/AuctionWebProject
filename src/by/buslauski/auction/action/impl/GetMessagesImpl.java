package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
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
     * Gets user's messages from database.
     *
     * @param request user's request.
     * @return null.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            ArrayList<UserMessage> messages = messageService.findUserMessages(user.getUserId());
            user.setUserMessages(messages);
            messageService.changeMessageStatus(user.getUserId());
            user.setUnreadMessages(false);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return null;
    }
}
