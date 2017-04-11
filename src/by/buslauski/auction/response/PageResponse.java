package by.buslauski.auction.response;

/**
 * Created by Acer on 31.03.2017.
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
