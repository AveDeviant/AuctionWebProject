package by.buslauski.auction.action;

import by.buslauski.auction.action.impl.*;
import by.buslauski.auction.action.impl.customer.*;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikita Buslauski
 */
public class CommandFactory {
    private static final String COMMAND_PARAM = "command";
    private static final String LOCALE = "locale";
    private static final String LOGIN = "authorization";
    private static final String REGISTRATION = "registration";
    private static final String LOGOUT = "logout";
    private static final String ADD_LOT = "addLot";
    private static final String GET_LOTS = "getLots";
    private static final String SHOW_LOT = "showLot";
    private static final String MAKE_BET = "makeBet";
    private static final String ORDER_LOT = "order";
    private static final String ORDER_LOT_BUY = "buy";
    private static final String ORDER_LOT_REJECT = "reject";
    private static final String EDIT_LOT = "editLot";
    private static final String GET_USERS = "getUsers";
    private static final String EDIT_ACCESS = "editAccess";
    private static final String LOTS_BY_CATEGORY = "lotsByCategory";
    private static final String GO_TO_PAGE = "goTo";
    private static final String GET_ORDERS = "getOrders";
    private static final String MESSAGE = "message";
    private static final String GET_CATEGORIES = "getCategories";
    private static final String DELETE_LOT = "deleteLot";
    private static final String GET_MESSAGES = "getMessages";
    private static final String ADD_CATEGORY = "addCategory";
    private static final String LOT_STATUS = "lotStatus";
    private static final String USER_OPERATIONS = "getOperations";
    private static final String TRADER_RATING = "updateRating";
    private static final String BACK_BUTTON = "back";
    private static final String TRADER_LOTS = "traderLots";
    private static final String USER_LOTS = "userLots";
    private static final String EXTEND_PERIOD = "extendPeriod";

    public Command getCurrentCommand(HttpServletRequest request) {
        String command = request.getParameter(COMMAND_PARAM);
        if (command == null || command.isEmpty()) {
            return new InitCommandImpl();
        }
        switch (command) {
            case LOCALE:
                return new LocaleCommandImpl();
            case LOGIN:
                return new AuthorizationCommandImpl();
            case REGISTRATION:
                return new RegistrationCommandImpl();
            case LOGOUT:
                return new LogoutCommandImpl();
            case ADD_LOT:
                return new AddLotImpl();
            case GET_LOTS:
                return manageAccessAdmin(request, new GetLotsImpl());
            case SHOW_LOT:
                return new ShowLotImpl();
            case MAKE_BET:
                return new BetCommandImpl();
            case ORDER_LOT:
                return manageAccessAuthorizedUser(request, new OrderPageImpl());
            case ORDER_LOT_BUY:
                return manageAccessAuthorizedUser(request, new BuyLotImpl());
            case ORDER_LOT_REJECT:
                return manageAccessAuthorizedUser(request, new RejectOrderImpl());
            case EDIT_LOT:
                return manageAccessAdmin(request, new LotEditImpl());
            case GET_USERS:
                return manageAccessAdmin(request, new GetUsersImpl());
            case EDIT_ACCESS:
                return manageAccessAdmin(request, new AccessEditImpl());
            case LOTS_BY_CATEGORY:
                return new GetLotsByCategoryImpl();
            case GO_TO_PAGE:
                return new GoToCommandImpl();
            case GET_ORDERS:
                return manageAccessAdmin(request, new GetOrdersCommandImpl());
            case MESSAGE:
                return new MessageCommandImpl();
            case GET_CATEGORIES:
                return new GetCategoriesImpl();
            case DELETE_LOT:
                return manageAccessAdmin(request, new DeleteLotImpl());
            case GET_MESSAGES:
                return manageAccessAuthorizedUser(request, new GetMessagesImpl());
            case ADD_CATEGORY:
                return manageAccessAdmin(request, new AddCategoryImp());
            case LOT_STATUS:
                return manageAccessAuthorizedUser(request, new LotStatusEditImpl());
            case USER_OPERATIONS:
                return manageAccessAuthorizedUser(request, new GetUserOperationsImpl());
            case TRADER_RATING:
                return manageAccessAuthorizedUser(request, new TraderRatingImpl());
            case BACK_BUTTON:
                return new BackCommandImpl();
            case TRADER_LOTS:
                return new GetLotsByTraderImpl();
            case USER_LOTS:
                return manageAccessAuthorizedUser(request, new GetUserLotsImpl());
            case EXTEND_PERIOD:
                return manageAccessAuthorizedUser(request, new ExtendBiddingPeriodImpl());
            default:
                return new InitCommandImpl();
        }
    }

    private Command manageAccessAuthorizedUser(HttpServletRequest request, Command potentialCommand) {
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        return user == null ? new InitCommandImpl() : potentialCommand;
    }

    private Command manageAccessAdmin(HttpServletRequest request, Command potentialCommand) {
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        return user == null || user.getRole() != Role.ADMIN ? new InitCommandImpl() : potentialCommand;
    }
}
