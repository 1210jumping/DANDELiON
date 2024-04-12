package com.example.myeducationapp.DAO.CourseDAO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * @author u7532738 Jinhan Tan
 * Course class
 */
public class Course implements Serializable,Comparable<Course>{
    // basic field of course
    //id is primary key
    String id;
    String cno;
    String cname;
    String imgUrl;
    String subject;
    String lecturer;
    String content;
    String releaseDate;


    // field used to sort the course
    int sortOrder = 1; // 1 is sort by cno, 2 is sort by date
    int sortBy = 1; // 1 is asc , other is desc


    /**
     *
     * @param id
     * @param cname
     * @param imgUrl
     * @param subject
     * @param lecturer
     * @param releaseDate
     */
    public Course(String id,String cname,String imgUrl, String subject, String lecturer,String releaseDate){
        this.id=id;
        this.cname=cname;
        this.imgUrl=imgUrl;
        this.subject=subject;
        this.lecturer=lecturer;
        this.releaseDate=releaseDate;
        this.content="This is a course in "+subject+" given by lecturer "+lecturer+" , it is mainly about "+cname+" !!";
        Random random = new Random();
        int randomNumber = random.nextInt(8000) + 1000;
        this.cno=subject.substring(0,2).toUpperCase()+randomNumber;
    }

    /**
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return String
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
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }


    /**
     *
     * @return sortOrder
     */
    public int getSortOrder() {
        return sortOrder;
    }


    /**
     *
     * @return date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @return course name
     */
    public String getCname() {
        return cname;
    }

    /**
     *
     * @return cno
     */
    public String getCno() {
        return cno;
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
     * @return image URL
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     *
     * @return lecturer name
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     *
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param sortBy
     */
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    /**
     *
     * @param sortOrder
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }


    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     *
     * @return
     */
    public int getSortBy() {
        return sortBy;
    }

    /**
     *
     * @param sortBy
     * @param sortOrder
     */
    public void setSortOrder(int sortBy, int sortOrder){
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
    }

    /**
     * if sortOrder is larger than 0, it is sort in asc order, otherwise in desc order
     * if sortBy is 1 sort by course number, else sort by datetime
     * @param course
     * @return
     */
    @Override
    public int compareTo(Course course) {
        if(this.equals(course))return 0;
        switch (sortBy) {
            case 1:
                if (sortOrder >= 0) {
                    return this.getCno().compareTo(course.getCno());
                } else {
                    return course.getCno().compareTo(this.getCno());
                }
            case 2:
                if (sortOrder >= 0) {
                    return this.getReleaseDate().compareTo(course.getReleaseDate());
                } else {
                    return course.getReleaseDate().compareTo(this.getReleaseDate());
                }
            default:
                return 1;
        }
    }

}
