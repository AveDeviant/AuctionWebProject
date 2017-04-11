package by.buslauski.auction.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Acer on 20.03.2017.
 */
public class AmountGenerator {

    /**
     * Bank balance imitation.
     *
     * @return BigDecimal
     */
    public BigDecimal generateMoneyAmount() {
        Random random = new Random();
        double money = random.nextInt(20000) * random.nextDouble();
        BigDecimal moneyAmount = new BigDecimal(money);
        moneyAmount.setScale(2, BigDecimal.ROUND_CEILING);
        return moneyAmount;
    }
}
