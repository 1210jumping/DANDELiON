package com.example.myeducationapp;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationapp.DAO.CommentDAO.Comment;
import com.example.myeducationapp.DAO.CommentDAO.CommentDao;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.CourseDAO.CourseDao;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.DAO.UserDAO.UserDao;
import com.example.myeducationapp.Tree.RBTree;
import com.example.myeducationapp.ui.course.CourseJoinActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author u7532738 Jinhan Tan
 * Global class
 */
public class Global extends Application {
    // local file path that store user, course and comment
    public final static String commentPath = "app/src/main/assets/comment.xml";
    public final static String contentPath = "app/src/main/assets/content.csv";
    public final static String userPath = "app/src/main/assets/user.json";

    public static ArrayList<User>userList=new ArrayList<>();// all users
    public static ArrayList<Course>courseList=new ArrayList<>();// all courses
    public static ArrayList<Comment>commentList=new ArrayList<>(); // all comments
    public static RBTree<Course> courseRBTree=new RBTree<>();//course RBTree
    public static RBTree<Comment>commentRBTree=new RBTree<>();//comment RBTree
    // Map to store course and its comments
    public static Map<String,RBTree<Comment>>courseComment=new HashMap<>();
    //map which store enroll info
    public static Map<String,List<String>> enrollInfo=new HashMap<>();

    //enroll course, key is course id and value is enroll id
    public static HashMap<String,String>enrollCourse=new HashMap<>();
    // my course set
    public static Set<Course> myCourseList=new HashSet<>();

    // the unique login user
    public static MyUser currentUser;

    //firebase user
    public static FirebaseUser firebaseUser;
    // some drawables which is picture
    public static final List<Integer> drawables = Arrays.asList(
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
            R.drawable.avatar_6,
            R.drawable.avatar_7,
            R.drawable.avatar_8,
            R.drawable.avatar_9,
            R.drawable.avatar_10,
            R.drawable.avatar_11,
            R.drawable.avatar_12,
            R.drawable.avatar_13,
            R.drawable.avatar_14,
            R.drawable.avatar_15,
            R.drawable.avatar_16);

    // user head pictures
    public static final List<Integer> headAvatars = Arrays.asList(
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
            R.drawable.avatar_6,
            R.drawable.avatar_7,
            R.drawable.avatar_8,
            R.drawable.avatar_9,
            R.drawable.avatar_10,
            R.drawable.avatar_11,
            R.drawable.avatar_12,
            R.drawable.avatar_13,
            R.drawable.avatar_14,
            R.drawable.avatar_15,
            R.drawable.avatar_16);

    //course pictures
    public static final List<Integer> courseAvatars = Arrays.asList(
            R.drawable.course_1,
            R.drawable.course_2,
            R.drawable.course_3,
            R.drawable.course_4,
            R.drawable.course_5,
            R.drawable.course_6,
            R.drawable.course_7,
            R.drawable.course_8,
            R.drawable.course_9,
            R.drawable.course_10,
            R.drawable.course_11,
            R.drawable.course_12,
            R.drawable.course_13,
            R.drawable.course_14,
            R.drawable.course_15,
            R.drawable.course_16,
            R.drawable.course_17,
            R.drawable.course_18);




    /**
     *  get the activity from former activity
     * @param intent
     * @return
     */
    public static Activity getActivityFromIntent(Intent intent) {
        try {
            Log.d("0","Enter");
            ComponentName componentName = intent.getComponent();
            Log.d("1",componentName.toString());
            String className = componentName.getClassName();
            Log.d("2",className);
            Class<?> cls = Class.forName(className);
            Log.d("3",cls.toString());
            return (Activity) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * set all user list
     * @param user
     */
    public static void setUserList(ArrayList<User>user){
        userList=user;
    }


    /**
     * set all course list
     * @param course
     */
    public static void setCourseList(ArrayList<Course>course){
        courseList=course;
    }

    public static void main(String[] args) {
    }

    /**
     * update information
     * including:
     * userlist
     * courselist
     * commentlist
     * enroll information
     * notify adapter
     */
    public static void update(){
        new UserDao().findAllUsers();
        new CourseDao().findAllCourses();
        new CommentDao().findAllComments();
        new CourseDao().getCourseEnroll();
        new CourseDao().getUserCourse();
        new CourseJoinActivity().update();
    }


    /**
     * enroll user to a course
     * and store it in the firebase
     * @param currentCourse
     */
    public static void enroll(Course currentCourse){
        String uid=Global.currentUser.getId();
        String cid=currentCourse.getId();
        Global.myCourseList.add(currentCourse);
        HashMap<String, Object> enroll = new HashMap<>();
        enroll.put("uid", uid);
        enroll.put("cid", cid);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("enroll").document().set(enroll)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Global.update();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * user drop a course and update it to the firebase
     * @param currentCourse
     */
    public static void drop(Course currentCourse){
        String uid=Global.currentUser.getId();
        String cid=currentCourse.getId();
        HashMap<String, Object> enroll = new HashMap<>();
        enroll.put("uid", uid);
        enroll.put("cid", cid);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Global.myCourseList.remove(currentCourse);
        db.collection("enroll").document(Global.enrollCourse.get(cid)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Global.enrollCourse.remove(cid);
                    Global.update();
                }else {
                    System.out.println("drop course error!");
                }
            }
        });
    }

    /**
     * get user by id
     * @param uid
     * @return
     */
    public static User getUserById(String uid){
        for(int i=0;i<userList.size();i++){
            if(userList.get(i).getId().equals(uid))
                return userList.get(i);
        }
        return null;
    }

}
