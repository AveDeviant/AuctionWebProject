package by.buslauski.auction.dao;

import by.buslauski.auction.entity.Comment;
import by.buslauski.auction.dao.exception.DAOException;

import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public interface CommentDao {

    void addComment(long userId, long lotId, String content) throws DAOException;

    ArrayList<Comment> getCommentsByLotId(long lotId) throws DAOException;
}
