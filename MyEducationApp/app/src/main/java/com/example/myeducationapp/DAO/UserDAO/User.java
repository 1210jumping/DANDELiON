package com.example.myeducationapp.DAO.UserDAO;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author u7532738 Jinhan Tan
 * User class
 */
public class User implements Serializable{

    // id is primary key
    String id;
    String name;
    String email;
    int sex;//0 female,1 male, 2 other
    String pwd;
    String imgURL;

    String country;

    /**
     * constructor
     * @param id
     * @param name
     * @param email
     * @param sex
     * @param pwd
     * @param imgURL
     */
    public User(String id,String name,String email,int sex,String pwd,String imgURL){
        this.id=id;
        this.name=name;
        this.email=email;
        this.sex=sex;
        this.pwd=pwd;
        this.imgURL=imgURL;
        this.country = "Australia";
    }

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
        User user = (User) o;
        return id == user.id;
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
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", pwd='" + pwd + '\'' +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }


    /**
     *
     * @return sex of user
     */
    public int getSex() {
        return sex;
    }

    /**
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return get img
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * get pwd
     * @return
     */
    public String getPwd() {
        return pwd;
    }

    /**
     *
     * @param imgURL
     */
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     *
     * @param sex
     */
    public void setSex(int sex) {this.sex = sex;}

    /**
     *
     * @return
     */
    public String getId(){
        return this.id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getCountry() {return country;}

    /**
     *
     * @param country
     */
    public void setCountry(String country) {this.country = country;}
}
