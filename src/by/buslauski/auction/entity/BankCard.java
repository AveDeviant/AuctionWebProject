package by.buslauski.auction.entity;

import java.math.BigDecimal;

/**
 * Created by Acer on 18.03.2017.
 */
public class BankCard {
    private long cardId;
    private long userId;
    private String cardSystem;
    private String cardNumber;
    private BigDecimal moneyAmount;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCardSystem() {
        return cardSystem;
    }

    public void setCardSystem(String cardSystem) {
        this.cardSystem = cardSystem;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(cardNumber.substring(0,5));
        stringBuilder.append("XXXX-XXXX-XXXX");
        return cardSystem +": " + stringBuilder.toString();
    }

}
