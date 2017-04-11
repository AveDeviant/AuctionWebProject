package by.buslauski.auction.action;

import by.buslauski.auction.constant.RequestAttributes;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
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
 * Created by Acer on 16.03.2017.
 */
public class AddLotImpl implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LOT_TITLE = "title";
    private static final String LOT_DESCRIPTION = "description";
    private static final String LOT_PRICE = "price";
    private static final String LOT_IMAGE = "image";
    private static final String LOT_CATEGORY = "category";
    private static final String UPLOAD = "/LotImg";
    private static final String IMAGE_MIME_TYPE = "image/";
    private static final String USER_SESSION = "user";
    private static final String AVAILABLE_TIMER = "availableTiming";
    private static final String IMAGE_ERROR = "imageErr";
    private static final String OFFER_ERROR = "offerErr";
    private static final String ADD_ERROR = "addErr";

    @Override
    public PageResponse execute(HttpServletRequest request) {
        String page = request.getParameter(RequestAttributes.JSP_PATH);
        PageResponse pageResponse = new PageResponse();
        String controller = request.getRequestURI();
        if (page.endsWith("?")) {
            pageResponse.setPage(page);
        } else {
            String query = page.substring(page.lastIndexOf("?"));
            pageResponse.setPage(controller + query);
        }
        try {
            User user = (User) request.getSession().getAttribute(USER_SESSION);
            long userId = user.getUserId();
            if (user.getBankCard() == null) {
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.setAttribute(OFFER_ERROR, ResponseMessage.OFFER_REGISTER_CARD);
                return pageResponse;
            }
            String lotTitle = request.getParameter(LOT_TITLE);
            String lotPrice = request.getParameter(LOT_PRICE);
            String lotDescription = request.getParameter(LOT_DESCRIPTION);
            String lotCategory = request.getParameter(LOT_CATEGORY);
            boolean availability = false;
            String lotTimer = request.getParameter(AVAILABLE_TIMER);
            if (Role.ADMIN.getValue().equals(user.getRole().getValue())) {
                lotTimer = request.getParameter(AVAILABLE_TIMER);
                availability = true;
            }
            Part lotImage = request.getPart(LOT_IMAGE);
            String fileName = lotImage.getSubmittedFileName();
            if (!fileName.isEmpty()) {
                String type = request.getServletContext().getMimeType(fileName);
                if (!type.startsWith(IMAGE_MIME_TYPE)) {
                    request.setAttribute(IMAGE_ERROR, ResponseMessage.INVALID_IMAGE_TYPE);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
            }
            String uploadPath = request.getServletContext().getRealPath(UPLOAD);
            Uploading uploading = new Uploading();
            String image = UPLOAD + "/" + uploading.uploadFile(uploadPath, lotImage);
            LotService logic = new LotService();
            logic.addLot(lotTitle, userId, lotDescription, image, new BigDecimal(lotPrice), availability, lotCategory, lotTimer);
            pageResponse.setResponseType(ResponseType.REDIRECT);
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ADD_ERROR, ResponseMessage.OPERATION_ERROR);
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during uploading file");
        }
        return pageResponse;
    }
}
