package by.buslauski.auction.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents info about entity "result_notification".
 *
 * @author Mikita Buslauski
 */
public class AuctionNotification {

    /**
     * Unique identifier of auction result/notification.
     */
    private long notificationId;

    /**
     * Identifier of the user on whose lot the buyer was found.
     */
    private long traderId;

    /**
     * Identifier of the user who win the auction.
     */
    private long customerId;

    /**
     * Identifier of the winning lot.
     */
    private long lotId;

    /**
     * Alias of the auction winner.
     */
    private String customerAlias;

    /**
     * Title of the winning lot.
     */
    private String lotTitle;

    /**
     * Time of auction result/notification registration.
     */
    private LocalDateTime dateTime;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(long traderId) {
        this.traderId = traderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public String getCustomerAlias() {
        return customerAlias;
    }

    public void setCustomerAlias(String customerAlias) {
        this.customerAlias = customerAlias;
    }

    public String getLotTitle() {
        return lotTitle;
    }

    public void setLotTitle(String lotTitle) {
        this.lotTitle = lotTitle;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionNotification that = (AuctionNotification) o;

        if (notificationId != that.notificationId) return false;
        if (traderId != that.traderId) return false;
        if (customerId != that.customerId) return false;
        if (lotId != that.lotId) return false;
        if (!customerAlias.equals(that.customerAlias)) return false;
        if (!lotTitle.equals(that.lotTitle)) return false;
        return dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId, traderId, customerId, customerAlias, lotId, lotTitle, dateTime);
    }
}
