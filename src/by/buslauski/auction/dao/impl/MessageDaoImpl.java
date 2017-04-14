package by.buslauski.auction.dao.impl;


import by.buslauski.auction.dao.MessageDao;
import by.buslauski.auction.entity.UserMessage;
import by.buslauski.auction.entity.User;
import by.buslauski.auction.exception.DAOException;
import by.buslauski.auction.util.DateTimeParser;
import org.apache.logging.log4j.Level;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Acer on 31.03.2017.
 */
public class MessageDaoImpl extends AbstractDao implements MessageDao {
    private static final String SQL_ADD_MESSAGE = "INSERT INTO message VALUES(NULL,?,?,?,?,NOW(),?,?,FALSE)";
    private static final String SQL_SELECT_USER_MESSAGES = "SELECT * FROM message WHERE id_recipient=? OR id_sender=? ORDER BY id_message";
    private static final String SQL_COUNT_UNREAD_MESSAGES = "SELECT COUNT(*) AS count FROM message WHERE id_recipient=? " +
            "AND readed=FALSE";
    private static final String SQL_RESET_UNREAD_STATUS = "UPDATE message SET readed=TRUE WHERE id_recipient=?";

    @Override
    public void addMessage(String theme, String text, User sender, User recipient) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_MESSAGE)) {
            preparedStatement.setLong(1, sender.getUserId());
            preparedStatement.setLong(2, recipient.getUserId());
            preparedStatement.setString(3, theme);
            preparedStatement.setString(4, text);
            preparedStatement.setString(5, sender.getUserName());
            preparedStatement.setString(6, recipient.getUserName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    @Override
    public ArrayList<UserMessage> findUserMessages(long userId) throws DAOException {
        ArrayList<UserMessage> messages = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_MESSAGES)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(initMessage(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return messages;
    }

    @Override
    public long countUserUnreadMessages(long userId) throws DAOException {
        long count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_UNREAD_MESSAGES)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getLong("count");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
        return count;
    }

    @Override
    public void changeMessageStatus(long userId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_RESET_UNREAD_STATUS)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DAOException(e);
        }
    }

    private UserMessage initMessage(ResultSet resultSet) throws SQLException {
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageId(resultSet.getLong("id_message"));
        userMessage.setSenderId(resultSet.getLong("id_sender"));
        userMessage.setRecipientId(resultSet.getLong("id_recipient"));
        userMessage.setTheme(resultSet.getString("theme"));
        userMessage.setContent(resultSet.getString("content"));
        userMessage.setSenderUsername(resultSet.getString("sender_username"));
        userMessage.setRecipientUsername(resultSet.getString("recipient_username"));
        userMessage.setDateTime(DateTimeParser.parseDate(resultSet.getString("date")));
        return userMessage;
    }


}
