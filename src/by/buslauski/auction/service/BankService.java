package by.buslauski.auction.service;

import by.buslauski.auction.entity.BankCard;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.exception.ServiceException;

import java.math.BigDecimal;

/**
 * Created by Acer on 14.04.2017.
 */
public interface BankService {
    BankCard addAccount(long userId, String system, String cardNumber) throws ServiceException;

    boolean checkIsEnoughBalance(long userId, BigDecimal enteredPrice) throws ServiceException;

    boolean doPayment(Bet bet) throws ServiceException;
}
