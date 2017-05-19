package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.constant.ResponseMessage;
import by.buslauski.auction.constant.SessionAttributes;
import by.buslauski.auction.entity.Lot;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.response.PageResponse;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.service.CommentService;
import by.buslauski.auction.service.LotService;
import by.buslauski.auction.service.impl.CommentServiceImpl;
import by.buslauski.auction.service.impl.LotServiceImpl;
import by.buslauski.auction.servlet.Controller;
import by.buslauski.auction.util.NumberParser;
import by.buslauski.auction.validator.MessageValidator;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Buslauski Mikita
 */
public class CommentImpl implements Command {
    private static final String LOT_ID = "lotId";
    private static final String CONTENT = "content";
    private static final String AUTHORIZATION_ERROR = "authorizationError";
    private static final String COMMENT_ERROR = "error";
    private static LotService lotService = new LotServiceImpl();
    private static CommentService commentService = new CommentServiceImpl();

    /**
     * Handling client request.
     *
     * @param request client request to get parameters to work with.
     * @return {@link PageResponse} object containing fields {@link ResponseType} and {@link String}
     * for {@link Controller}.
     * ResponseType - response type (forward or redirect).
     * String page - page for response.
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(Command.returnPageWithQuery(request));
        pageResponse.setResponseType(ResponseType.FORWARD);
        User currentUser = (User) request.getSession().getAttribute(SessionAttributes.USER);
        if (currentUser == null) {
            request.setAttribute(AUTHORIZATION_ERROR, ResponseMessage.MESSAGE_ERROR_AUTHORIZATION);
            pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
            return pageResponse;
        }
        String content = request.getParameter(CONTENT);
        long lotId = NumberParser.parse(request.getParameter(LOT_ID));
        try {
            Lot lot = lotService.getAvailableLotById(lotId);
            if (lot != null) {
                if (MessageValidator.checkComment(content)) {
                    commentService.addComment(currentUser.getUserId(), lot.getId(), content);
                    pageResponse.setResponseType(ResponseType.REDIRECT);
                } else {
                    pageResponse.setResponseType(ResponseType.FORWARD);
                    request.setAttribute(COMMENT_ERROR, ResponseMessage.INVALID_VALUE);
                }
            } else {
                pageResponse.setPage(PageNavigation.PAGE_NOT_FOUND);
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            pageResponse.setResponseType(ResponseType.FORWARD);
            request.setAttribute(COMMENT_ERROR, ResponseMessage.OPERATION_ERROR);
        }

        return pageResponse;
    }
}
