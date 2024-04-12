package com.example.myeducationapp.DAO.UserDAO;

import android.content.Context;

import java.util.List;

/**
 * @author u7532738 Jinhan Tan
 * UserDaoInterface
 */
public interface UserDaoInterface {

    void update(Context context,User user);
    void findAllUsers();
    User findUserById(String id);

    List<String>getAllUserEmails(Context context);

}
