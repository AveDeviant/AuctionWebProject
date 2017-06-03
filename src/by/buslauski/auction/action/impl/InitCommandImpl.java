package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.MessageService;
import by.buslauski.auction.service.impl.AuctionServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.MessageServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class InitCommandImpl implements Command {
    private static final String AVAILABLE_LOTS = "lots";
    private static final String EMPTY_LIST = "emptyList";
    private static final String USER_BANNED = "banned";
    private static MessageService messageService = new MessageServiceImpl();
    private static UserService userService = new UserServiceImpl();
    private static LotService lotService = new LotServiceImpl();
    private static AuctionService auctionService = new AuctionServiceImpl();

    /**
     * Init command. Get <code>ArrayList</code> of {@link Lot} objects
     * with {@link Lot#availability}==true from database and displays it to main page.
     * Displaying appropriate message in case the list is empty.
     * Displaying appropriate message in case {@link User} have been banned.
     * Check for new events in case <code>User</code> has been authorized.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - response type: {@link ResponseType#FORWARD}
     * String page - page for response {@link PageNavigation#MAIN_PAGE}
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.MAIN_PAGE);
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            ArrayList<Lot> availableLots = lotService.getAvailableLots();
            if (user != null) {
                user = userService.findUserById(user.getUserId());  //update customer info
                user.setUserMessages(messageService.findUserMessages(user.getUserId()));
                if (messageService.haveUnreadMessages(user.getUserId())) {  // check  new messages for customer
                    user.setUnreadMessages(true);
                }
                request.getSession().setAttribute(SessionAttributes.USER, user);
            }
            if (user != null && user.getAccess()) {
                auctionService.setWinner(user);
                request.getSession().removeAttribute(USER_BANNED);
            }
            if (user != null && !user.getAccess()) {
                request.getSession().setAttribute(USER_BANNED, ResponseMessage.USER_BANNED);
            }
            if (!availableLots.isEmpty()) {
                request.setAttribute(AVAILABLE_LOTS, availableLots);
            } else {
                request.setAttribute(EMPTY_LIST, ResponseMessage.EMPTY_LOT_LIST);
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
