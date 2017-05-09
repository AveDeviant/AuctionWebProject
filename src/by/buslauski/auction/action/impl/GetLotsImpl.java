package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Acer on 18.03.2017.
 */
public class GetLotsImpl implements Command {
    private static final String ALL_LOTS = "allLots";
    private static final String EMPTY_LIST = "emptyList";
    private static LotService lotService = new LotServiceImpl();

    /**
     * Get all lots from database.
     *
     * @param request user's request.
     * @return null.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        try {
            ArrayList<Lot> allLots = lotService.getAllLots();
            if (allLots.isEmpty()) {
                request.setAttribute(EMPTY_LIST, ResponseMessage.EMPTY_LOT_LIST);
            } else {
                request.setAttribute(ALL_LOTS, allLots);
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e + " Error during getting lots from database");
        }
        return null;
    }
}
