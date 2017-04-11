package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 25.03.2017.
 */
public class RejectCommandImpl implements Command {
    private static final String ORDER_ERROR_ATTR = "orderError";
    private static LotService lotService = new LotService();

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(RequestAttributes.USER);
        Bet bet = user.getWinningBets().get(0);
        String controller = request.getRequestURI();
        String path = request.getParameter(RequestAttributes.JSP_PATH);
        if (path.endsWith("?")) {
            pageResponse.setPage(path);
        } else {
            String query = path.substring(path.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
        try {
            Lot lot = lotService.getLotById(bet.getLotId());
            lotService.resetBids(lot);
            user.getWinningBets().remove(bet);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
        } catch (ServiceException e) {
            request.setAttribute(ORDER_ERROR_ATTR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setPage(path);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}