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
}
