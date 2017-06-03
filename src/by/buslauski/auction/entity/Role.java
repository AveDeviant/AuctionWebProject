package by.buslauski.auction.entity;

/**
 * This enum represents info about entity "role".
 *
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
