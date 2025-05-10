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
 * Global 類別，繼承自 Application，用於管理應用程式的全局狀態和資料。
 * 此類別包含應用程式中共享的資料，例如使用者列表、課程列表、評論列表等，
 * 並提供相關的操作方法。
 */
public class Global extends Application {
    // 本地檔案路徑，用於儲存使用者、課程和評論資料
    /** 儲存評論資料的 XML 檔案路徑 */
    public final static String commentPath = "app/src/main/assets/comment.xml";
    /** 儲存內容資料的 CSV 檔案路徑 */
    public final static String contentPath = "app/src/main/assets/content.csv";
    /** 儲存使用者資料的 JSON 檔案路徑 */
    public final static String userPath = "app/src/main/assets/user.json";

    /** 儲存所有使用者物件的 ArrayList */
    public static ArrayList<User>userList=new ArrayList<>();
    /** 儲存所有課程物件的 ArrayList */
    public static ArrayList<Course>courseList=new ArrayList<>();
    /** 儲存所有評論物件的 ArrayList */
    public static ArrayList<Comment>commentList=new ArrayList<>();
    /** 用於儲存課程資料的紅黑樹結構，可能用於高效搜尋 */
    public static RBTree<Course> courseRBTree=new RBTree<>();
    /** 用於儲存評論資料的紅黑樹結構，可能用於高效搜尋 */
    public static RBTree<Comment>commentRBTree=new RBTree<>();
    /**
     * 一個 Map，用於儲存課程 ID 與其對應評論（以紅黑樹結構儲存）的映射。
     * 鍵 (String) 是課程 ID，值 (RBTree<Comment>) 是該課程的評論紅黑樹。
     */
    public static Map<String,RBTree<Comment>>courseComment=new HashMap<>();
    /**
     * 一個 Map，用於儲存選課資訊。
     * 鍵 (String) 可能是課程 ID 或使用者 ID，值 (List<String>) 是一個字串列表，
     * 可能代表選修該課程的使用者 ID 列表或某使用者選修的課程 ID 列表。
     */
    public static Map<String,List<String>> enrollInfo=new HashMap<>();

    /**
     * 一個 HashMap，用於追蹤選課記錄。
     * 鍵 (String) 是課程 ID，值 (String) 是該選課記錄在 Firebase 中的文件 ID。
     */
    public static HashMap<String,String>enrollCourse=new HashMap<>();
    /** 一個 Set，用於儲存目前使用者已選修的課程，確保課程不重複 */
    public static Set<Course> myCourseList=new HashSet<>();

    /** 代表目前登入應用程式的使用者物件 */
    public static MyUser currentUser;

    /** 代表透過 Firebase Authentication 認證的目前使用者物件 */
    public static FirebaseUser firebaseUser;
    /**
     * 一個包含 drawable 資源 ID 的列表，這些資源是圖片。
     * 用於應用程式中一般的頭像或圖片顯示。
     */
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

    /**
     * 一個包含使用者頭像 drawable 資源 ID 的列表。
     */
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

    /**
     * 一個包含課程圖片 drawable 資源 ID 的列表。
     */
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
     *  嘗試從傳入的 Intent 中獲取其指定的 Activity 類別，並透過反射機制建立該 Activity 的新實例。
     * @param intent 包含目標 Activity 組件資訊的 Intent 物件。
     * @return 如果成功，則回傳新建立的 Activity 實例；若發生錯誤（例如找不到類別、實例化失敗等），則回傳 null。
     */
    public static Activity getActivityFromIntent(Intent intent) {
        try {
            Log.d("0","Enter getActivityFromIntent"); // 記錄方法進入點
            ComponentName componentName = intent.getComponent();
            Log.d("1",componentName.toString()); // 記錄 ComponentName
            String className = componentName.getClassName();
            Log.d("2",className); // 記錄類別名稱
            Class<?> cls = Class.forName(className); // 使用反射載入類別
            Log.d("3",cls.toString()); // 記錄 Class 物件
            return (Activity) cls.newInstance(); // 建立實例並轉型為 Activity
        } catch (Exception e) {
            e.printStackTrace(); // 列印錯誤堆疊資訊
            return null; // 發生例外時回傳 null
        }
    }


    /**
     * 設定全域的使用者列表。
     * @param user 包含所有使用者物件的 ArrayList。
     */
    public static void setUserList(ArrayList<User>user){
        userList=user;
    }


    /**
     * 設定全域的課程列表。
     * @param course 包含所有課程物件的 ArrayList。
     */
    public static void setCourseList(ArrayList<Course>course){
        courseList=course;
    }

    public static void main(String[] args) {
    }

    /**
     * 更新應用程式的全局資料。
     * 此方法會重新載入使用者、課程、評論以及選課相關的資訊。
     * 通常在資料發生變更後呼叫，以確保全局資料是最新的。
     * 它也會通知 {@link CourseJoinActivity} 更新其介面。
     */
    public static void update(){
        new UserDao().findAllUsers(); // 從資料來源獲取所有使用者
        new CourseDao().findAllCourses(); // 從資料來源獲取所有課程
        new CommentDao().findAllComments(); // 從資料來源獲取所有評論
        new CourseDao().getCourseEnroll(); // 從資料來源獲取課程的選課資訊
        new CourseDao().getUserCourse(); // 從資料來源獲取使用者的課程資訊
        new CourseJoinActivity().update(); // 通知 CourseJoinActivity 更新
    }


    /**
     * 將目前登入的使用者註冊到指定的課程。
     * 此方法會將選課資訊儲存到 Firebase Firestore，並更新本地的全局資料。
     * @param currentCourse 要註冊的課程物件。
     */
    public static void enroll(Course currentCourse){
        String uid=Global.currentUser.getId(); // 獲取目前使用者的 ID
        String cid=currentCourse.getId(); // 獲取課程的 ID
        Global.myCourseList.add(currentCourse); // 將課程加入到目前使用者的選課列表

        HashMap<String, Object> enroll = new HashMap<>(); // 建立用於儲存到 Firebase 的資料
        enroll.put("uid", uid);
        enroll.put("cid", cid);

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 FirebaseFirestore 實例
        // 將選課資訊寫入 Firebase 的 "enroll" 集合中，自動產生文件 ID
        db.collection("enroll").document().set(enroll)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Global.update(); // 選課成功後更新全局資料
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * 讓目前登入的使用者退出指定的課程。
     * 此方法會從 Firebase Firestore 中刪除對應的選課記錄，並更新本地的全局資料。
     * @param currentCourse 要退選的課程物件。
     */
    public static void drop(Course currentCourse){
        String uid=Global.currentUser.getId(); // 獲取目前使用者的 ID
        String cid=currentCourse.getId(); // 獲取課程的 ID

        HashMap<String, Object> enroll = new HashMap<>();
        enroll.put("uid", uid);
        enroll.put("cid", cid);

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 FirebaseFirestore 實例
        Global.myCourseList.remove(currentCourse); // 從目前使用者的選課列表中移除課程

        String enrollId = Global.enrollCourse.get(cid); // 從 enrollCourse Map 中獲取選課記錄的 Firebase 文件 ID

        // 從 Firebase 的 "enroll" 集合中刪除對應的選課文件
        db.collection("enroll").document(enrollId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Global.enrollCourse.remove(cid); // 從 enrollCourse Map 中移除該課程的選課記錄
                    Global.update(); // 退選成功後更新全局資料
                } else {
                    System.out.println("drop course error!"); // 在Console輸出錯誤訊息
                }
            }
        });
    }

    /**
     * 根據使用者 ID 從全局使用者列表 ({@code userList}) 中查找並回傳對應的使用者物件。
     * @param uid 要查找的使用者 ID (字串)。
     * @return 如果找到對應的使用者，則回傳 {@link User} 物件；如果未找到，則回傳 {@code null}。
     */
    public static User getUserById(String uid) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user != null && uid.equals(user.getId())) { // 檢查 user 物件是否為空，並比較 ID
                return user;
            }
        }
        return null; // 遍歷完畢未找到則回傳 null
    }

}
