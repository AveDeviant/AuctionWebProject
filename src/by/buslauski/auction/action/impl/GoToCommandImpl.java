package by.buslauski.auction.action.impl;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.constant.PageNavigation;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Acer on 30.03.2017.
 */
public class GoToCommandImpl implements Command {
    private static final String PAGE = "page";
    private static final String AUTHORIZATION = "authorization";
    private static final String OFFER_LOT = "offer";
    private static final String REGISTRATION = "registration";
    private static final String INDEX = "index";
    private static final String EDIT_USER = "editUser";
    private static final String EDIT_LOT = "editLot";
    private static final String PERSONAL_PAGE = "private";
    private static final String ADMIN_PAGE = "admin";
    private static final String FAQ_PAGE = "faq";
    private static final String MESSAGE_PAGE = "message";
    private static final String SUCCESS_PAGE ="success";

    /**
     * Page navigation.
     *
     * @param request
     * @return An object containing two fields:
     * ResponseType- forward or redirect
     * page - page for response
     */
    @Override
    public PageResponse execute(HttpServletRequest request) {
        PageResponse pageResponse = new PageResponse();

        pageResponse.setResponseType(ResponseType.FORWARD);
        switch (request.getParameter(PAGE)) {
            case AUTHORIZATION:
                pageResponse.setPage(PageNavigation.AUTHORIZATION_PAGE);
                return pageResponse;
            case OFFER_LOT:
                pageResponse.setPage(PageNavigation.OFFER_LOT);
                return pageResponse;
            case REGISTRATION:
                pageResponse.setPage(PageNavigation.REGISTRATION_PAGE);
                return pageResponse;
            case INDEX:
                pageResponse.setPage(PageNavigation.INDEX_PAGE);
                return pageResponse;
            case EDIT_LOT:
                pageResponse.setPage(PageNavigation.LOT_EDIT_PAGE);
                return pageResponse;
            case EDIT_USER:
                pageResponse.setPage(PageNavigation.USER_EDIT_PAGE);
                return pageResponse;
            case PERSONAL_PAGE:
                pageResponse.setPage(PageNavigation.PRIVATE_PAGE);
                return pageResponse;
            case ADMIN_PAGE:
                pageResponse.setPage(PageNavigation.ADMIN_PAGE);
                return pageResponse;
            case FAQ_PAGE:
                pageResponse.setPage(PageNavigation.FAQ_PAGE);
                return pageResponse;
            case MESSAGE_PAGE:
                pageResponse.setPage(PageNavigation.MESSAGE_PAGE);
                return pageResponse;
            case SUCCESS_PAGE:
                pageResponse.setPage(PageNavigation.SUCCESS_PAGE);
                return pageResponse;
            default:
                pageResponse.setPage(PageNavigation.INDEX_PAGE);
                return pageResponse;
        }
    }
}
