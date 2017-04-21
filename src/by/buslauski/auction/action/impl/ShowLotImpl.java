package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.UserService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 19.03.2017.
 */
public class ShowLotImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String LOT = "lot";
    private static final String AUCTION_OWNER = "auctionOwner";
    private static LotService lotService = new LotServiceImpl();
    private static UserService userService = new UserServiceImpl();

    /**
     * Showing lot info.
     * Showing notification to user in case auction is owner of this lot.
     *
     * @param request
     * @return PageResponse - an object containing two fields:
     * ResponseType   Forward
     * String page - /jsp/lot.jsp
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        long lotId = Long.parseLong(request.getParameter(LOT_ID));
        try {
            Lot lot = lotService.getAvailableLotById(lotId);
            if (lot == null) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
                return pageResponse;
            }
            User user = userService.findUserById(lot.getUserId());
            if (Role.ADMIN == user.getRole()) {
                request.setAttribute(AUCTION_OWNER, "данный лот является собственностью аукциона." +
                        "В случае выигрыша этого лота, денежные средства будут списаны с Вашего счета" +
                        " при подтвержении сделки Вами с одной стороны и аукциона с другой");
            }
            request.setAttribute(LOT, lot);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.LOT_PAGE);
        } catch (ServiceException e) {
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            LOGGER.log(Level.ERROR, e);
        }
        return pageResponse;
    }
}
