package by.buslauski.auction.tag;

import by.buslauski.auction.entity.Bet;

import java.util.ArrayList;

/**
 * Created by Acer on 26.03.2017.
 */
public class CustomerBet {
    private ArrayList<Bet> bets;
    private int counter = 0;

    public CustomerBet(ArrayList<Bet> bets) {
        this.bets = bets;
    }

    public String showBetInfo() {
        if (!bets.isEmpty() && counter < bets.size()) {
            Bet bet = bets.get(counter);
            counter++;
            return bet.getLotTitle() + " " + bet.getBet() + " " + bet.getDate();
        }
        return "";
    }
}
