package by.buslauski.auction.action;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 21.02.2017.
 */
public class CommandFactory {
    private static final String COMMAND_PARAM = "command";
    private static final String LOCALE = "locale";
    private static final String BACK = "back";
    private static final String LOGIN = "authorization";
    private static final String REGISTRATION = "registration";
    private static final String LOGOUT = "logout";
    private static final String ADD_LOT = "addLot";
    private static final String GET_LOTS = "getLots";
    private static final String SHOW_LOT = "showLot";
    private static final String MAKE_BET = "makeBet";
    private static final String CREATE_USER_FUND = "addBankAccount";
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
    private static final String GET_MESSAGES ="getMessages";
    private static final String ADD_CATEGORY="addCategory";

    public Command getCurrentCommand(HttpServletRequest request) {
        String command = request.getParameter(COMMAND_PARAM);
        if (command == null || command.isEmpty()) {
            return new InitCommandImpl();
        }
        switch (command) {
            case LOCALE:
                return new LocaleCommandImpl();
//            case BACK:
//                return new BackCommandImpl();
            case LOGIN:
                return new AuthorizationCommandImpl();
            case REGISTRATION:
                return new RegistrationCommandImpl();
            case LOGOUT:
                return new LogoutCommandImp();
            case ADD_LOT:
                return new AddLotImpl();
            case GET_LOTS:
                return new GetLotsImpl();
            case SHOW_LOT:
                return new ShowLotImpl();
            case CREATE_USER_FUND:
                return new AddBankCardImpl();
            case MAKE_BET:
                return new BetCommandImpl();
            case ORDER_LOT:
                return new OrderCommandImpl();
            case ORDER_LOT_BUY:
                return new BuyCommandImpl();
            case ORDER_LOT_REJECT:
                return new RejectCommandImpl();
            case EDIT_LOT:
                return new LotEditImpl();
            case GET_USERS:
                return new GetUsersImpl();
            case EDIT_ACCESS:
                return new AccessEditImpl();
            case LOTS_BY_CATEGORY:
                return new GetLotsByCategoryImpl();
            case GO_TO_PAGE:
                return new GoToCommandImpl();
            case GET_ORDERS:
                return new GetOrdersCommandImpl();
            case MESSAGE:
                return new MessageCommandImpl();
            case GET_CATEGORIES:
                return new GetCategoriesImpl();
            case DELETE_LOT:
                return new DeleteLotImpl();
            case GET_MESSAGES:
                return new GetMessagesImpl();
            case ADD_CATEGORY:
                return new AddCategoryImp();
            default:
                return new InitCommandImpl();
        }
    }
}
