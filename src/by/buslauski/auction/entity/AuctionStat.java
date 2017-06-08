package by.buslauski.auction.entity;

import java.math.BigDecimal;

/**
 * This class represents info about total auction statistic.
 *
 * @author Mikita Buslauski
 */
public class AuctionStat {

    /**
     * Total sum of all accepted orders.
     */
    private BigDecimal dealsSum;

    /**
     * Total count of all accepted order.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionStat that = (AuctionStat) o;

        if (dealsCount != that.dealsCount) return false;
        return dealsSum.equals(that.dealsSum);
    }

    @Override
    public int hashCode() {
        int result = dealsSum.hashCode();
        result = 31 * result + (int) (dealsCount ^ (dealsCount >>> 32));
        return result;
    }
}
