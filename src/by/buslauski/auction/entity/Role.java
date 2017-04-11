package by.buslauski.auction.entity;

/**
 * Created by Acer on 17.03.2017.
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
