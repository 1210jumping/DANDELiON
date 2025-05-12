package com.example.myeducationapp.DAO.CommentDAO;

import java.io.Serializable;
import java.util.Objects;


/**
 * Comment 類別，代表一則評論。
 * 實作了 Serializable 以便序列化，以及 Comparable 以便依日期排序。
 */
public class Comment implements Serializable,Comparable<Comment>{
    // 評論的基本欄位
    String commentId; // 評論 ID
    // courseId 是外鍵，關聯到課程
    String courseId; // 課程 ID
    String userId; // 使用者 ID
    String content; // 評論內容
    String releaseDate; // 發布日期

    /**
     * Comment 的建構子。
     *
     * @param commentId 評論的唯一識別碼。
     * @param userId 發表評論的使用者 ID。
     * @param courseId 評論所屬的課程 ID。
     * @param content 評論的文字內容。
     * @param releaseDate 評論的發布日期。
     */
    public Comment(String commentId,String userId,String courseId,String content,String releaseDate){
        this.commentId=commentId;
        this.courseId=courseId;
        this.userId=userId;
        this.content=content;
        this.releaseDate=releaseDate;
    }

    /**
     * 獲取評論的發布日期。
     *
     * @return 發布日期字串。
     */

    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * 獲取評論的內容。
     *
     * @return 評論內容字串。
     */

    public String getContent() {
        return content;
    }


    /**
     * 回傳 Comment 物件的字串表示。
     *
     * @return 代表 Comment 物件的字串。
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
     * 獲取評論的 ID。
     *
     * @return 評論 ID 字串。
     */
    public String getCommentId() {
        return commentId;
    }


    /**
     * 獲取評論所屬的課程 ID。
     *
     * @return 課程 ID 字串。
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * 獲取發表評論的使用者 ID。
     *
     * @return 使用者 ID 字串。
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 比較此 Comment 物件與指定物件是否相等。
     * 如果兩個 Comment 物件的 commentId 相同，則視為相等。
     *
     * @param o 要比較的物件。
     * @return 如果物件相等則回傳 true，否則回傳 false。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 如果是同一個物件，則相等
        if (o == null || getClass() != o.getClass()) return false; // 如果物件為 null 或類別不同，則不相等
        Comment comment = (Comment) o; // 將物件轉換為 Comment 型別
        return Objects.equals(commentId, comment.commentId); // 比較 commentId 是否相等
    }

    /**
     * 回傳 Comment 物件的雜湊碼。
     * 雜湊碼是基於 commentId 計算的。
     *
     * @return 此物件的雜湊碼值。
     */
    @Override
    public int hashCode() {
        return Objects.hash(commentId); // 使用 commentId 計算雜湊碼
    }


    /**
     * 依發布日期比較兩個 Comment 物件。
     * 用於排序評論。
     *
     * @param comment 要比較的另一個 Comment 物件。
     * @return 如果此評論的發布日期早於指定評論，則回傳負整數；如果晚於，則回傳正整數；如果相同，則回傳零。
     */
    @Override
    public int compareTo(Comment comment) {
        return this.getReleaseDate().compareTo(comment.getReleaseDate()); // 比較兩個評論的發布日期
    }
}