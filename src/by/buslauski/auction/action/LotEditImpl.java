package by.buslauski.auction.action;

import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.Uploading;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Acer on 27.03.2017.
 */
public class LotEditImpl implements Command {
    private static final String LOT_ID = "id";
    private static final String LOT_TITLE = "title";
    private static final String STARTING_PRICE = "price";
    private static final String AVAILABLE_TIMING = "availableTiming";
    private static final String AVAILABILITY = "availability";
    private static final String EDIT_ERROR = "editErr";
    private static final String UPLOAD = "/LotImg";
    private static final String IMAGE_MIME_TYPE = "image/";
    private static final String LOT_IMAGE = "image";
    private static final String CATEGORY = "category";
    private static final Logger LOGGER = LogManager.getLogger();
    private static LotService lotService = new LotServiceImpl();

    /**
     * @param request
     * @return Array of two strings:
     * array[0] - response type (forward or redirect)
     * array[1] - path for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user =(User) request.getSession().getAttribute(RequestAttributes.USER);
        if (user==null || !Role.ADMIN.getValue().equals(user.getRole().getValue())){
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            return pageResponse;
        }
        long id = Long.valueOf(request.getParameter(LOT_ID));
        try {
            Lot lot = lotService.getLotById(id);
            if (lot == null) {
                request.setAttribute(EDIT_ERROR, ResponseMessage.LOT_ID_DOESNT_EXISTS);
                pageResponse.setResponseType(ResponseType.FORWARD);
                pageResponse.setPage(request.getParameter(RequestAttributes.JSP_PATH));
                return pageResponse;
            }
            Part lotImage = request.getPart(LOT_IMAGE);
            String fileName = lotImage.getSubmittedFileName();
            if (!fileName.isEmpty()) {
                String type = request.getServletContext().getMimeType(fileName);
                if (!type.startsWith(IMAGE_MIME_TYPE)) {
                    request.setAttribute(EDIT_ERROR, ResponseMessage.INVALID_IMAGE_TYPE);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    pageResponse.setPage(request.getParameter(RequestAttributes.JSP_PATH));
                    return pageResponse;
                }
            }
            String uploadPath = request.getServletContext().getRealPath(UPLOAD);
            Uploading uploading = new Uploading();
            String image = UPLOAD + "/" + uploading.uploadFile(uploadPath, lotImage);
            String lotTitle = request.getParameter(LOT_TITLE);
            String startingPrice = request.getParameter(STARTING_PRICE);
            boolean availability = Boolean.parseBoolean(request.getParameter(AVAILABILITY));
            String availableDate = request.getParameter(AVAILABLE_TIMING);
            String category = request.getParameter(CATEGORY);
            lotService.editLot(id, category, lotTitle, image, new BigDecimal(startingPrice), availability, availableDate);
            pageResponse.setResponseType(ResponseType.REDIRECT);
            String controller = request.getRequestURI();
            String path = request.getParameter(RequestAttributes.JSP_PATH);
            if (path.endsWith("?")) {
                pageResponse.setPage(path);
            } else {
                String query = path.substring(path.lastIndexOf("?"));
                pageResponse.setPage(controller + query);
            }
        } catch (ServiceException e) {
            request.setAttribute(EDIT_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
            pageResponse.setPage(request.getParameter(RequestAttributes.JSP_PATH));
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, e + "Exception during uploading image");
        }
        return pageResponse;
    }
}
