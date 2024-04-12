package com.example.myeducationapp.DAO.CommentDAO;

import java.io.Serializable;
import java.util.Objects;


/**
 * @author u7532738 Jinhan Tan
 * Comment class
 */
public class Comment implements Serializable,Comparable<Comment>{
    //basic field of course
    String commentId;
    // courseId is the primary key
    String courseId;
    String userId;
    String content;
    String releaseDate;

    /**
     *
     * @param commentId
     * @param userId
     * @param courseId
     * @param content
     * @param releaseDate
     */
    public Comment(String commentId,String userId,String courseId,String content,String releaseDate){
        this.commentId=commentId;
        this.courseId=courseId;
        this.userId=userId;
        this.content=content;
        this.releaseDate=releaseDate;
    }

    /**
     *
     * @return released date
     */

    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @return content
     */

    public String getContent() {
        return content;
    }


    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    /**
     *
     * @return commnetId
     */
    public String getCommentId() {
        return commentId;
    }


    /**
     *
     * @return courseId
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     *
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param o comment to be compared
     * @return false if not equal, true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId;
    }

    /**
     * get hashcode
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }


    /**
     * compare two comments by their released date
     * @param comment
     * @return
     */
    @Override
    public int compareTo(Comment comment) {
        return this.getReleaseDate().compareTo(comment.getReleaseDate());
    }
}
