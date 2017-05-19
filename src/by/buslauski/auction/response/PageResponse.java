package by.buslauski.auction.response;

/**
 * @author Mikita Buslauski
 */
public class PageResponse {
    private ResponseType responseType;
    private String page;

    public void setResponseType(ResponseType responseType) {
        this.responseType=responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setPage(String page) {
        this.page=page;
    }

    public String getPage(){
        return page;
    }
}
