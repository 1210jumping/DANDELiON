package com.example.myeducationapp.DAO.UserDAO;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * User 類別，代表一個使用者。
 * 實作了 Serializable 以便序列化。
 */
public class User implements Serializable{

    // id 是主鍵
    String id; // 使用者 ID
    String name; // 使用者名稱
    String email; // 使用者電子郵件
    int sex; // 性別：0 代表女性，1 代表男性，2 代表其他
    String pwd; // 使用者密碼
    String imgURL; // 使用者頭像圖片 URL

    String country; // 使用者所在國家

    /**
     * User 的建構子。
     *
     * @param id 使用者的唯一識別碼。
     * @param name 使用者的名稱。
     * @param email 使用者的電子郵件地址。
     * @param sex 使用者的性別 (0: 女性, 1: 男性, 2: 其他)。
     * @param pwd 使用者的密碼。
     * @param imgURL 使用者頭像圖片的 URL。
     */
    public User(String id,String name,String email,int sex,String pwd,String imgURL){
        this.id=id;
        this.name=name;
        this.email=email;
        this.sex=sex;
        this.pwd=pwd;
        this.imgURL=imgURL;
        this.country = "Australia"; // 預設國家為澳洲
    }

    /**
     * 建立並回傳此物件的副本。
     *
     * @return 此物件的副本。
     * @throws CloneNotSupportedException 如果物件的類別不支援 Cloneable 介面。
     */
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 比較此 User 物件與指定物件是否相等。
     * 如果兩個 User 物件的 id 相同，則視為相等。
     *
     * @param o 要比較的物件。
     * @return 如果物件相等則回傳 true，否則回傳 false。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 如果是同一個物件，則相等
        if (o == null || getClass() != o.getClass()) return false; // 如果物件為 null 或類別不同，則不相等
        User user = (User) o; // 將物件轉換為 User 型別
        return id.equals(user.id); // 比較 id 是否相等 (注意：原始碼中使用 == 比較字串，建議改為 equals)
    }

    /**
     * 回傳 User 物件的雜湊碼。
     * 雜湊碼是基於 id 計算的。
     *
     * @return 此物件的雜湊碼值。
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); // 使用 id 計算雜湊碼
    }

    /**
     * 回傳 User 物件的字串表示。
     *
     * @return 代表 User 物件的字串。
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
     * 獲取使用者的性別。
     *
     * @return 使用者的性別 (0: 女性, 1: 男性, 2: 其他)。
     */
    public int getSex() {
        return sex;
    }

    /**
     * 獲取使用者的電子郵件地址。
     *
     * @return 使用者的電子郵件地址字串。
     */
    public String getEmail() {
        return email;
    }

    /**
     * 獲取使用者頭像圖片的 URL。
     *
     * @return 使用者頭像圖片的 URL 字串。
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * 獲取使用者的密碼。
     *
     * @return 使用者的密碼字串。
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 設定使用者頭像圖片的 URL。
     *
     * @param imgURL 新的頭像圖片 URL 字串。
     */
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    /**
     * 設定使用者的名稱。
     *
     * @param name 新的使用者名稱字串。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 設定使用者的密碼。
     *
     * @param pwd 新的密碼字串。
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 設定使用者的性別。
     *
     * @param sex 新的性別 (0: 女性, 1: 男性, 2: 其他)。
     */
    public void setSex(int sex) {this.sex = sex;}

    /**
     * 獲取使用者的 ID。
     *
     * @return 使用者的 ID 字串。
     */
    public String getId(){
        return this.id;
    }

    /**
     * 獲取使用者的名稱。
     *
     * @return 使用者的名稱字串。
     */
    public String getName() {
        return name;
    }

    /**
     * 獲取使用者所在的國家。
     *
     * @return 使用者所在國家的字串。
     */
    public String getCountry() {return country;}

    /**
     * 設定使用者所在的國家。
     *
     * @param country 新的國家字串。
     */
    public void setCountry(String country) {this.country = country;}
}