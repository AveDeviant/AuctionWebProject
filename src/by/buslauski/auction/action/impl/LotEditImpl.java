package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.Role;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.InvalidDateValueException;
import by.buslauski.auction.exception.InvalidNumberValueException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.FileUploadingManager;
import by.buslauski.auction.validator.BetValidator;
import by.buslauski.auction.validator.LotValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

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
    private static LotService lotService = new LotServiceImpl();

    /**
     * Editing lot title, starting price, image, availability for bids,
     * timing, lot category.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - REDIRECT in case operation passed successfully and FORWARD
     * in other case.
     * String page - page for response (current page).
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (user == null || !Role.ADMIN.getValue().equals(user.getRole().getValue())) {
            pageResponse.setResponseType(ResponseType.REDIRECT);
            pageResponse.setPage(PageNavigation.INDEX_PAGE);
            return pageResponse;
        }
        pageResponse.setPage(request.getParameter(SessionAttributes.JSP_PATH));
        pageResponse.setResponseType(ResponseType.FORWARD);
        long id = Long.valueOf(request.getParameter(LOT_ID));
        try {
            Lot lot = lotService.getLotById(id);
            if (lot == null) {
                request.setAttribute(EDIT_ERROR, ResponseMessage.LOT_ID_DOESNT_EXISTS);
                return pageResponse;
            }
            Part lotImage = request.getPart(LOT_IMAGE);
            String fileName = lotImage.getSubmittedFileName();
            if (!fileName.isEmpty()) {
                String type = request.getServletContext().getMimeType(fileName);
                if (!type.startsWith(IMAGE_MIME_TYPE)) {
                    request.setAttribute(EDIT_ERROR, ResponseMessage.INVALID_IMAGE_TYPE);
                    pageResponse.setPage(request.getParameter(SessionAttributes.JSP_PATH));
                    return pageResponse;
                }
            }
            String uploadPath = request.getServletContext().getRealPath(UPLOAD);
            FileUploadingManager fileUploadingManager = new FileUploadingManager();
            String image = UPLOAD + File.separator + fileUploadingManager.uploadFile(uploadPath, lotImage);
            String lotTitle = request.getParameter(LOT_TITLE);
            String startingPrice = request.getParameter(STARTING_PRICE);
            boolean availability = Boolean.parseBoolean(request.getParameter(AVAILABILITY));
            String availableDate = request.getParameter(AVAILABLE_TIMING);
            String category = request.getParameter(CATEGORY);
            if (LotValidator.checkLot(lotTitle, availableDate) && BetValidator.checkPriceForValid(startingPrice)) {
                lotService.editLot(id, category, lotTitle, image, BetValidator.initPrice(startingPrice), availability, availableDate);
                pageResponse.setResponseType(ResponseType.REDIRECT);
            } else {
                pageResponse.setResponseType(ResponseType.FORWARD);
                request.setAttribute(EDIT_ERROR, ResponseMessage.INVALID_VALUE);
            }
            pageResponse.setPage(returnPageWithQuery(request));
        } catch (ServiceException e) {
            request.setAttribute(EDIT_ERROR, ResponseMessage.OPERATION_ERROR);
            pageResponse.setResponseType(ResponseType.FORWARD);
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, e + "Exception during uploading image");
        } catch (InvalidNumberValueException e) {
            request.setAttribute(EDIT_ERROR, ResponseMessage.INVALID_VALUE);
            pageResponse.setResponseType(ResponseType.FORWARD);
        } catch (InvalidDateValueException e) {
            request.setAttribute(EDIT_ERROR, ResponseMessage.INVALID_DATE_VALUE);
            pageResponse.setResponseType(ResponseType.FORWARD);
        }
        return pageResponse;
    }
}
