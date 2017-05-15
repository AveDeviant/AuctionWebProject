package by.buslauski.auction.entity;

/**
 * @author Mikita Buslauski
 */
public enum Role {
    CUSTOMER("customer"), ADMIN("admin");

    private String value;

   Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
