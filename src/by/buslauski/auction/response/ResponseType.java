package by.buslauski.auction.response;

/**
 * @author Mikita Buslauski
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
