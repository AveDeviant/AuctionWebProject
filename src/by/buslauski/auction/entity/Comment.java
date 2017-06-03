package by.buslauski.auction.entity;

import java.time.LocalDateTime;

/**
 * This class represents info about entity "comment".
 *
 * @author Mikita Buslauski
 */
public class Comment {
    private long commentId;
    private long lotId;
    private long userId;
    private String userAlias;
    private String content;
    private LocalDateTime time;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (commentId != comment.commentId) return false;
        if (lotId != comment.lotId) return false;
        if (userId != comment.userId) return false;
        if (!userAlias.equals(comment.userAlias)) return false;
        if (!content.equals(comment.content)) return false;
        return time.equals(comment.time);
    }

    @Override
    public int hashCode() {
        int result = (int) (commentId ^ (commentId >>> 32));
        result = 31 * result + (int) (lotId ^ (lotId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + userAlias.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}
