package by.buslauski.auction.entity;

import java.math.BigDecimal;

/**
 * This class represents info about total auction statistic.
 *
 * @author Mikita Buslauski
 */
public class AuctionStat {
    private BigDecimal dealsSum;
    private long dealsCount;

    public BigDecimal getDealsSum() {
        return dealsSum;
    }

    public void setDealsSum(BigDecimal dealsSum) {
        this.dealsSum = dealsSum;
    }

    public long getDealsCount() {
        return dealsCount;
    }

    public void setDealsCount(long dealsCount) {
        this.dealsCount = dealsCount;
    }


}
