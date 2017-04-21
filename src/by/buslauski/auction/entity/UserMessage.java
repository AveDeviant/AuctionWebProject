package by.buslauski.auction.entity;

import java.time.LocalDateTime;

/**
 * Created by Acer on 31.03.2017.
 */
public class UserMessage {
    private long messageId;
    private long senderId;
    private long recipientId;
    private String senderUsername;
    private String RecipientUsername;
    private String theme;
    private String content;
    private LocalDateTime dateTime;


    public long getMessageId(){
        return messageId;
    }
    public void setMessageId(long messageId){
        this.messageId=messageId;
    }
    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return RecipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        RecipientUsername = recipientUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMessage that = (UserMessage) o;

        if (messageId != that.messageId) return false;
        if (senderId != that.senderId) return false;
        if (recipientId != that.recipientId) return false;
        if (!senderUsername.equals(that.senderUsername)) return false;
        if (!RecipientUsername.equals(that.RecipientUsername)) return false;
        if (theme != null ? !theme.equals(that.theme) : that.theme != null) return false;
        if (!content.equals(that.content)) return false;
        return dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (messageId ^ (messageId >>> 32));
        result = 31 * result + (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (int) (recipientId ^ (recipientId >>> 32));
        result = 31 * result + senderUsername.hashCode();
        result = 31 * result + RecipientUsername.hashCode();
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + content.hashCode();
        result = 31 * result + dateTime.hashCode();
        return result;
    }
}
