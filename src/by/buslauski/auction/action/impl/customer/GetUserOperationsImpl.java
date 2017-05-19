package by.buslauski.auction.action.impl.customer;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Order;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.BetService;
import by.buslauski.auction.service.OrderService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.BetServiceImpl;
import by.buslauski.auction.service.impl.OrderServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class GetUserOperationsImpl implements Command {
    private static final String USER_ORDERS = "orders";
    private static final String USER_BETS = "bets";
    private static final String TRADER_RATING = "rating";
    private static OrderService orderService = new OrderServiceImpl();
    private static UserService userService = new UserServiceImpl();
    private static BetService betService = new BetServiceImpl();

    /**
     * Get customer bets, accepted deals and lots from database
     * and display it to current customer.
     *
     * @param request client request to get parameters to work with.
     * @return null.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        try {
            ArrayList<Order> orders = orderService.getUserConfirmedOrders(currentUser.getUserId());
            ArrayList<Integer> rating = userService.defineRating();
            ArrayList<Bet> bets = betService.getUserBets(currentUser);
            request.setAttribute(USER_ORDERS, orders);
            request.setAttribute(TRADER_RATING, rating);
            request.setAttribute(USER_BETS, bets);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return null;
    }
}
