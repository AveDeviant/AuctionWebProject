package by.buslauski.auction.dao.impl;

import by.buslauski.auction.dao.CommentDao;
import by.buslauski.auction.entity.Comment;
import by.buslauski.auction.dao.exception.DAOException;
import by.buslauski.auction.util.DateTimeParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class CommentDaoImpl extends AbstractDao implements CommentDao {
    private static final String SQL_INSERT_COMMENT = "INSERT INTO comment VALUES(NULL,?,?,?,NOW())";
    private static final String SQL_GET_COMMENTS_BY_LOT = "SELECT id_comment, comment.id_user, alias, id_lot, content, time " +
            "FROM comment " +
            "JOIN user ON comment.id_user = user.id_user " +
            "WHERE id_lot=?";

    @Override
    public void addComment(long userId, long lotId, String content) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_COMMENT)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, lotId);
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public ArrayList<Comment> getCommentsByLotId(long lotId) throws DAOException {
        ArrayList<Comment> comments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_COMMENTS_BY_LOT)) {
            preparedStatement.setLong(1, lotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comments.add(initComment(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return comments;
    }

    private Comment initComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(resultSet.getLong("id_comment"));
        comment.setLotId(resultSet.getLong("id_lot"));
        comment.setUserId(resultSet.getLong("id_user"));
        comment.setUserAlias(resultSet.getString("alias"));
        comment.setContent(resultSet.getString("content"));
        comment.setTime(DateTimeParser.parseDate(resultSet.getString("time")));
        return comment;
    }

}
