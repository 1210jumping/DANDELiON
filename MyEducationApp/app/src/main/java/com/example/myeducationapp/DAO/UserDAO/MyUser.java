package com.example.myeducationapp.DAO.UserDAO;


/**
 * @author u7532738 Jinhan Tan
 * MyUser class
 */
public class MyUser extends User{

    private static MyUser instance = null;

    /**
     * singleton design pattern
     * @param user
     */
    public MyUser(User user) {
        super(user.getId(), user.getName(), user.getEmail(), user.getSex(), user.getPwd(), user.getImgURL());
    }


    /**
     *
     * @param user
     * @return
     */
    public static MyUser getInstance(User user){
        if(instance == null){
            instance = new MyUser(user);
        }
        return instance;
    }

    /**
     * login out user and set user = null
     */
    public void logoutUser(){
        instance = null;
    }
}
