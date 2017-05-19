package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.validator.exception.InvalidDateValueException;
import by.buslauski.auction.validator.exception.InvalidNumberValueException;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.service.FileUploadingManager;
import by.buslauski.auction.util.NumberParser;
import by.buslauski.auction.validator.BetValidator;
import by.buslauski.auction.validator.LotValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * @author Mikita Buslauski
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
     * <p>
     * Checked situations:
     * Invalid lot image extension;
     * Invalid lot title, description, price or date;
     * Exception during operation;
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link by.buslauski.auction.servlet.Controller}.
     * ResponseType - {@link ResponseType#REDIRECT} in case operation passed successfully and
     * {@link ResponseType#FORWARD} in other case.
     * String page - page for response (current page).
     * @see Command#returnPageWithQuery(HttpServletRequest)
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(Command.returnPageWithQuery(request));
        pageResponse.setResponseType(ResponseType.FORWARD);
        long id = NumberParser.parse(request.getParameter(LOT_ID));
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
            pageResponse.setPage(Command.returnPageWithQuery(request));
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
