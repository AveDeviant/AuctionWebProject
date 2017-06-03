package by.buslauski.auction.entity;

import java.time.LocalDateTime;

/**
 * This class represents info about entity "result_notification".
 *
 * @author Mikita Buslauski
 */
public class AuctionNotification {
    private long notificationId;
    private long traderId;
    private long customerId;
    private long lotId;
    private String customerAlias;
    private String lotTitle;
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


}
