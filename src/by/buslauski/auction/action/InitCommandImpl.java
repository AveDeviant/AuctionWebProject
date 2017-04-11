package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Acer on 01.04.2017.
 */
public class InitCommandImpl implements Command {
    private static final String AVAILABLE_LOTS = "lots";
    private static final String EMPTY_LIST = "emptyList";
    private static final String USER_BANNED = "banned";


    /**
     * Default command. Get available lots for bids from database and
     * displays it to main page. Displaying appropriate message if the list is empty.
     *
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        LotService lotService = new LotService();
        UserService userService = new UserService();
        PageResponse pageResponse = new PageResponse();
        pageResponse.setResponseType(ResponseType.FORWARD);
        pageResponse.setPage(PageNavigation.MAIN_PAGE);
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        try {
            ArrayList<Lot> availableLots = lotService.getAvailableLots();
            if (user != null && user.getAccess()) {
                userService.setWinner(user);
            }
            if (user != null && !user.getAccess()) {
                request.getSession().setAttribute(USER_BANNED, ResponseMessage.USER_BANNED);
            }
            if (availableLots.isEmpty()) {
                request.setAttribute(EMPTY_LIST, ResponseMessage.EMPTY_LOT_LIST);
            } else {
                request.setAttribute(AVAILABLE_LOTS, availableLots);
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
