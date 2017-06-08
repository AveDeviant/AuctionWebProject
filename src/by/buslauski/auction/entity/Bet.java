package by.buslauski.auction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents info about entity "bet".
 *
 * @author Mikita Buslauski
 */
public class Bet {

    /**
     * Unique identifier of the bet.
     */
    private long betId;

    /**
     * Identifier of the lot on which bet was made.
     */
    private long lotId;

    /**
     * Identifier of the user who made the bet.
     */
    private long userId;

    /**
     * Bet value.
     */
    private BigDecimal bet;

    /**
     * Time of bet registration.
     */
    private LocalDateTime date;

    /**
     * Title of the lot on which bet was made.
     */
    private String lotTitle;

    public Bet(long betId, long lotId, long userId, BigDecimal bet) {
        this.betId = betId;
        this.lotId = lotId;
        this.userId = userId;
        this.bet = bet;
    }

    public long getBetId() {
        return betId;
    }

    public void setBetId(long betId) {
        this.betId = betId;
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLotTitle(String lotTitle) {
        this.lotTitle = lotTitle;
    }

    public String getLotTitle() {
        return lotTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bet bet1 = (Bet) o;

        if (betId != bet1.betId) return false;
        if (lotId != bet1.lotId) return false;
        if (userId != bet1.userId) return false;
        if (!bet.equals(bet1.bet)) return false;
        if (!date.equals(bet1.date)) return false;
        return lotTitle.equals(bet1.lotTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(betId, lotId, userId, lotTitle, betId, date);
    }
}
