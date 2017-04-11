package by.buslauski.auction.response;

/**
 * Created by Acer on 19.03.2017.
 */
public enum ResponseType {
    FORWARD("forward"), REDIRECT("sendRedirect");

    private String value;

    ResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
