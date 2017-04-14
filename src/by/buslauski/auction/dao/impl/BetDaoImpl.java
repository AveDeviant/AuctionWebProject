package by.buslauski.auction.dao.impl;

import by.buslauski.auction.connection.ProxyConnection;
import by.buslauski.auction.dao.BetDao;
import by.buslauski.auction.entity.Bet;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.util.DateTimeParser;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 21.03.2017.
 */
public class BetDaoImpl extends AbstractDao implements BetDao {
    private static final String SQL_ADD_BET = "INSERT INTO bet VALUES (NULL,?,?,?,NOW())";
    private static final String SQL_SELECT_BETS_BY_LOT = "SELECT id_bet, id_lot, id_user, bet, date FROM bet" +
            " WHERE id_lot=?";
    private static final String SQL_SELECT_USER_BETS = "SELECT id_bet, bet.id_lot, bet.id_user, title, bet, date FROM " +
            "bet JOIN lot ON bet.id_lot=lot.id_lot WHERE bet.id_user=?";
    private static final String SQL_RESET_LOT_BETS = "DELETE FROM bet WHERE id_lot=?";

    public BetDaoImpl() {
    }

    public BetDaoImpl(ProxyConnection connection) {
        super(connection);
    }

    @Override
    public void addBet(long userId, long lotId, BigDecimal price) throws DAOException {
        try (PreparedStatement preparedStatementBet = connection.prepareStatement(SQL_ADD_BET)) {
            preparedStatementBet.setLong(1, lotId);
            preparedStatementBet.setLong(2, userId);
            preparedStatementBet.setBigDecimal(3, price);
            preparedStatementBet.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);

        }
    }

    @Override
    public ArrayList<Bet> getBetsByLotId(long lotId) throws DAOException {
        ArrayList<Bet> bets = new ArrayList<>();
        Bet bet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BETS_BY_LOT)) {
            preparedStatement.setLong(1, lotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bet = initBet(resultSet);
                bets.add(bet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException();
        }
        return bets;
    }

    public ArrayList<Bet> getUserBets(long userId) throws DAOException {
        ArrayList<Bet> userBets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BETS)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Bet bet = initBet(resultSet);
                bet.setLotTitle(resultSet.getString("title"));
                userBets.add(bet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return userBets;
    }

    public void resetBets(long lotId) throws DAOException {
        try (PreparedStatement preparedStatementBet = connection.prepareStatement(SQL_RESET_LOT_BETS)) {
            preparedStatementBet.setLong(1, lotId);
            preparedStatementBet.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    private Bet initBet(ResultSet resultSet) throws SQLException {
        Bet bet = new Bet(
                resultSet.getLong("id_bet"),
                resultSet.getLong("id_lot"),
                resultSet.getLong("id_user"),
                resultSet.getBigDecimal("bet")
        );
        bet.setDate(DateTimeParser.parseDate(resultSet.getString("date")));
        return bet;
    }

}
