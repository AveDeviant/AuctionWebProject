package by.buslauski.auction.action;

import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 19.03.2017.
 */
public class ShowLotImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String LOT = "lot";

    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        long lotId = Long.parseLong(request.getParameter(LOT_ID));
        LotService lotService = new LotServiceImpl();
        try {
            Lot lot = lotService.getAvailableLotById(lotId);
            if (lot == null) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
                return pageResponse;
            }
            request.setAttribute(LOT, lot);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(PageNavigation.LOT_PAGE);
        } catch (ServiceException e){
            LOGGER.log(Level.ERROR,e);
        }
        return pageResponse;
    }
}
