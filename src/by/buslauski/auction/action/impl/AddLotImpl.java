package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.exception.InvalidDateValueException;
import by.buslauski.auction.exception.InvalidNumberValueException;
import by.buslauski.auction.exception.ServiceException;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.PageBrowser;
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
 * @author Mikita Buslauski
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
    private static final String AVAILABLE_TIMER = "availableTiming";
    private static final String IMAGE_ERROR = "imageErr";
    private static final String ADD_ERROR = "addErr";
    private static LotService lotService = new LotServiceImpl();

    /**
     * TODO ЗАЩИТА ОТ ХИДДЕН ПОЛЕЙ
     */
    /**
     * Add a new lot and insert it into database.
     * Upload lot image on the server.
     * <p>
     * Checked situations:
     * User is unable to add a lot (customer have been banned);
     * Invalid lot image extension;
     * Invalid lot title, description, price or date;
     * Exception during operation;
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing two fields:
     * ResponseType - {@link ResponseType#REDIRECT} if operation passed successfully and
     * {@link ResponseType#FORWARD} in other case;
     * String page - page for response:
     * "/jsp/success.jsp" if operation passed successfully and current page with the appropriate message
     * in other case.
     * @see LotValidator
     * @see BetValidator
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(returnPageWithQuery(request));
        try {
            User user = (User) request.getSession().getAttribute(SessionAttributes.USER);
            if (!user.getAccess()) {
                pageResponse.setPage(definePathToAccessDeniedPage(request));
                pageResponse.setResponseType(ResponseType.REDIRECT);
                return pageResponse;
            }
            String lotTitle = request.getParameter(LOT_TITLE);
            String lotPrice = request.getParameter(LOT_PRICE);
            String lotDescription = request.getParameter(LOT_DESCRIPTION);
            String lotCategory = request.getParameter(LOT_CATEGORY);
            String lotTimer = request.getParameter(AVAILABLE_TIMER);
            Part lotImage = request.getPart(LOT_IMAGE);
            String fileName = lotImage.getSubmittedFileName();
            if (!fileName.trim().isEmpty()) {
                String type = request.getServletContext().getMimeType(fileName);
                if (!type.startsWith(IMAGE_MIME_TYPE)) {
                    request.setAttribute(IMAGE_ERROR, ResponseMessage.INVALID_IMAGE_TYPE);
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    return pageResponse;
                }
            }
            if (LotValidator.checkLot(lotTitle, lotDescription, lotTimer) && BetValidator.checkPriceForValid(lotPrice)) {
                String uploadPath = request.getServletContext().getRealPath(UPLOAD);
                FileUploadingManager fileUploadingManager = new FileUploadingManager();
                String image = UPLOAD + File.separator + fileUploadingManager.uploadFile(uploadPath, lotImage);
                lotService.addLot(user, lotTitle, lotDescription, image,  BetValidator.initPrice(lotPrice),
                        lotCategory, lotTimer);
                pageResponse.setResponseType(ResponseType.REDIRECT);
                PageBrowser browser = (PageBrowser) request.getSession().getAttribute(SessionAttributes.PAGE_BROWSER);
                browser.addPageToHistory(returnPageWithQuery(request));
                pageResponse.setPage(definePathToSuccessPage(request));
                return pageResponse;
            } else {
                request.setAttribute(ADD_ERROR, ResponseMessage.INVALID_VALUE);
                pageResponse.setResponseType(ResponseType.FORWARD);
            }
        } catch (ServiceException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ADD_ERROR, ResponseMessage.OPERATION_ERROR);
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, e + " Exception during uploading file");
        } catch (InvalidNumberValueException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ADD_ERROR, ResponseMessage.INVALID_BET_VALUE);
        } catch (InvalidDateValueException e) {
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(ADD_ERROR, ResponseMessage.INVALID_DATE_VALUE);
        }
        return pageResponse;
    }
}
