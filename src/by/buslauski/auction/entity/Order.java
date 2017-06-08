package by.buslauski.auction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents info about entity "order".
 *
 * @author Mikita Buslauski
 */
public class Order {
    /**
     * Unique identifier of order.
     */
    private long orderId;

    /**
     * Identifier of user who is a winner of the auction.
     */
    private long userId;

    /**
     * Identifier of user on whose lot the buyer was found.
     */
    private long traderId;

    /**
     * Identifier of the winning lot.
     */
    private long lotId;

    /**
     * Order cost, or lot final price.
     */
    private BigDecimal payment;

    /**
     * Time of order registration.
     */
    private LocalDateTime dateTime;

    /**
     * Order status: accepted or rejected.
     */
    private boolean accept;

    /**
     * Real purchaser's name.
     */
    private String costumerName;

    /**
     * Purchaser city.
     */
    private String costumerCity;

    /**
     * Purchaser address.
     */
    private String costumerAddress;

    /**
     * Purchaser contact phone.
     */
    private String costumerPhone;

    /**
     * Title of winning lot.
     */
    private String lotTitle;

    /**
     * Trader alias.
     */
    private String traderUsername;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(long traderId) {
        this.traderId = traderId;
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getCostumerName() {
        return costumerName;
    }

    public void setCostumerName(String costumerName) {
        this.costumerName = costumerName;
    }

    public String getCostumerCity() {
        return costumerCity;
    }

    public void setCostumerCity(String costumerCity) {
        this.costumerCity = costumerCity;
    }

    public String getCostumerAddress() {
        return costumerAddress;
    }

    public void setCostumerAddress(String costumerAddress) {
        this.costumerAddress = costumerAddress;
    }

    public String getCostumerPhone() {
        return costumerPhone;
    }

    public void setCostumerPhone(String costumerPhone) {
        this.costumerPhone = costumerPhone;
    }

    public String getLotTitle() {
        return lotTitle;
    }

    public void setLotTitle(String lotTitle) {
        this.lotTitle = lotTitle;
    }

    public String getTraderUsername() {
        return traderUsername;
    }

    public void setTraderUsername(String traderUsername) {
        this.traderUsername = traderUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (userId != order.userId) return false;
        if (traderId != order.traderId) return false;
        if (lotId != order.lotId) return false;
        if (accept != order.accept) return false;
        if (!payment.equals(order.payment)) return false;
        if (!dateTime.equals(order.dateTime)) return false;
        if (!costumerName.equals(order.costumerName)) return false;
        if (!costumerCity.equals(order.costumerCity)) return false;
        if (!costumerAddress.equals(order.costumerAddress)) return false;
        if (!costumerPhone.equals(order.costumerPhone)) return false;
        if (!lotTitle.equals(order.lotTitle)) return false;
        return traderUsername.equals(order.traderUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, traderId, lotId, accept, payment, dateTime, costumerName, costumerCity,
                costumerAddress, costumerPhone, traderUsername, lotTitle);
    }
}
