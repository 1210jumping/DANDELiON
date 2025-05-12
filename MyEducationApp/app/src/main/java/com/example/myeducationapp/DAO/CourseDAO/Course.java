package com.example.myeducationapp.DAO.CourseDAO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Course 類別，代表一門課程。
 * 實作了 Serializable 以便序列化，以及 Comparable 以便排序。
 */
public class Course implements Serializable,Comparable<Course>{
    // 課程的基本欄位
    // id 是主鍵
    String id; // 課程 ID
    String cno; // 課程編號
    String cname; // 課程名稱
    String imgUrl; // 圖片 URL
    String subject; // 科目
    String lecturer; // 講師
    String content; // 課程內容
    String releaseDate; // 發布日期


    // 用於排序課程的欄位
    int sortOrder = 1; // 排序順序：1 表示依 cno 排序，2 表示依日期排序
    int sortBy = 1; // 排序方式：1 表示升冪，其他表示降冪


    /**
     * Course 的建構子。
     *
     * @param id 課程的唯一識別碼。
     * @param cname 課程的名稱。
     * @param imgUrl 課程圖片的 URL。
     * @param subject 課程所屬的科目。
     * @param lecturer 課程的講師。
     * @param releaseDate 課程的發布日期。
     */
    public Course(String id,String cname,String imgUrl, String subject, String lecturer,String releaseDate){
        this.id=id;
        this.cname=cname;
        this.imgUrl=imgUrl;
        this.subject=subject;
        this.lecturer=lecturer;
        this.releaseDate=releaseDate;
        // 根據科目、講師和課程名稱產生預設的課程內容
        this.content="This is a course in "+subject+" given by lecturer "+lecturer+" , it is mainly about "+cname+" !!";
        // 產生一個隨機的課程編號 (cno)
        Random random = new Random();
        int randomNumber = random.nextInt(8000) + 1000; // 產生 1000 到 8999 之間的隨機數
        // 課程編號由科目前兩個字母的大寫和隨機數組成
        this.cno=subject.substring(0,2).toUpperCase()+randomNumber;
    }

    /**
     * 獲取課程 ID。
     *
     * @return 課程 ID 字串。
     */
    public String getId() {
        return id;
    }

    /**
     * 回傳 Course 物件的字串表示。
     *
     * @return 代表 Course 物件的字串。
     */
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", cno='" + cno + '\'' +
                ", cname='" + cname + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", subject='" + subject + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", content='" + content + '\'' +
                ", sortOrder=" + sortOrder +
                ", sortBy=" + sortBy +
                '}';
    }

    /**
     * 比較此 Course 物件與指定物件是否相等。
     * 如果兩個 Course 物件的 id 相同，則視為相等。
     *
     * @param o 要比較的物件。
     * @return 如果物件相等則回傳 true，否則回傳 false。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 如果是同一個物件，則相等
        if (o == null || getClass() != o.getClass()) return false; // 如果物件為 null 或類別不同，則不相等
        Course course = (Course) o; // 將物件轉換為 Course 型別
        return id.equals(course.id); // 比較 id 是否相等
    }


    /**
     * 獲取課程的排序順序。
     *
     * @return 排序順序的整數值 (1: 依 cno, 2: 依日期)。
     */
    public int getSortOrder() {
        return sortOrder;
    }


    /**
     * 獲取課程的發布日期。
     *
     * @return 發布日期字串。
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * 獲取課程名稱。
     *
     * @return 課程名稱字串。
     */
    public String getCname() {
        return cname;
    }

    /**
     * 獲取課程編號。
     *
     * @return 課程編號字串。
     */
    public String getCno() {
        return cno;
    }

    /**
     * 獲取課程內容。
     *
     * @return 課程內容字串。
     */
    public String getContent() {
        return content;
    }

    /**
     * 獲取課程圖片的 URL。
     *
     * @return 圖片 URL 字串。
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 獲取課程講師的名稱。
     *
     * @return 講師名稱字串。
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     * 獲取課程所屬的科目。
     *
     * @return 科目字串。
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 設定課程的排序方式。
     *
     * @param sortBy 排序方式 (1: 升冪, 其他: 降冪)。
     */
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * 設定課程的排序順序。
     *
     * @param sortOrder 排序順序 (1: 依 cno, 2: 依日期)。
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }


    /**
     * 回傳 Course 物件的雜湊碼。
     * 雜湊碼是基於 id 計算的。
     *
     * @return 此物件的雜湊碼值。
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); // 使用 id 計算雜湊碼
    }

    /**
     * 獲取課程的排序方式。
     *
     * @return 排序方式的整數值 (1: 升冪, 其他: 降冪)。
     */
    public int getSortBy() {
        return sortBy;
    }

    /**
     * 設定課程的排序方式和排序順序。
     *
     * @param sortBy 排序方式 (1: 升冪, 其他: 降冪)。
     * @param sortOrder 排序順序 (1: 依 cno, 2: 依日期)。
     */
    public void setSortOrder(int sortBy, int sortOrder){
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
    }

    /**
     * 比較兩個 Course 物件以進行排序。
     * 如果 sortOrder 大於等於 0，則為升冪排序，否則為降冪排序。
     * 如果 sortBy 為 1，則依課程編號 (cno) 排序，否則依發布日期排序。
     *
     * @param course 要比較的另一個 Course 物件。
     * @return 如果此課程應排在指定課程之前，則回傳負整數；如果應排在之後，則回傳正整數；如果相等，則回傳零。
     */
    @Override
    public int compareTo(Course course) {
        if(this.equals(course))return 0; // 如果課程相同，回傳 0
        switch (sortBy) {
            case 1: // 依課程編號排序
                if (sortOrder >= 0) { // 升冪
                    return this.getCno().compareTo(course.getCno());
                } else { // 降冪
                    return course.getCno().compareTo(this.getCno());
                }
            case 2: // 依發布日期排序
                if (sortOrder >= 0) { // 升冪
                    return this.getReleaseDate().compareTo(course.getReleaseDate());
                } else { // 降冪
                    return course.getReleaseDate().compareTo(this.getReleaseDate());
                }
            default: // 預設情況，可視為不相等或按某種固定順序
                return 1;
        }
    }

}