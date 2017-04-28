package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 01.04.2017.
 */
public class InitCommandImpl implements Command {
    private static final String AVAILABLE_LOTS = "lots";
    private static final String EMPTY_LIST = "emptyList";
    private static final String USER_BANNED = "banned";


    /**
     * Init command. Get available lots for bids from database and
     * displays it to main page. Displaying appropriate message in case the list is empty.
     *
     * @param request
     * @return An object PageResponse containing two fields:
     * ResponseType - response type (forward or redirect)
     * String page - page for response /jsp/main.jsp
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        LotService lotService = new LotServiceImpl();
        UserService userService = new UserServiceImpl();
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.MAIN_PAGE);
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            ArrayList<Lot> availableLots = lotService.getAvailableLots();
            if (user != null && user.getAccess()) {
                userService.setWinner(user);
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
