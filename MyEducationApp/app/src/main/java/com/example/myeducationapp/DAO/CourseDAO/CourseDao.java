package com.example.myeducationapp.DAO.CourseDAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.Tree.RBTree;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * CourseDao 類別，用於處理課程資料的存取操作。
 * 實作了 CourseDaoInterface 介面。
 */
public class CourseDao implements CourseDaoInterface {
    // Firebase Firestore 的實例，用於資料庫操作
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * 從 Firebase Firestore 中查詢所有課程。
     * 成功查詢後，會將課程資料儲存到全域變數 Global.courseList 和 Global.courseRBTree 中。
     */
    @Override
    public void findAllCourses() {
        ArrayList<Course> arrayList = new ArrayList<>();
        // 從 "courseList" 集合中獲取所有文件
        db.collection("courseList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                // 初始化全域課程紅黑樹
                Global.courseRBTree=new RBTree<>();
                // 遍歷查詢結果中的每個文件
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 獲取文件資料
                    Random r = new Random();
                    // 隨機選取一個課程圖片索引
                    int i = r.nextInt(Global.drawables.size());
                    Map<String, Object> data = documentSnapshot.getData();
                    // 處理資料
                    String cid = documentSnapshot.getId(); // 課程 ID
                    String cname = data.get("cname").toString(); // 課程名稱
                    String lecturer = data.get("lecturer").toString(); // 講師名稱
                    // 從全域課程頭像列表中隨機選取一個圖片 URL
                    String image = Global.courseAvatars.get(i % Global.courseAvatars.size()).toString();
                    String date = data.get("releaseDate").toString(); // 發布日期
                    String subject = data.get("subject").toString(); // 科目
                    // 建立 Course 物件
                    Course course = new Course(cid, cname, image, subject, lecturer, date);
                    arrayList.add(course); // 將課程加入列表
                    Global.courseRBTree.insert(course); // 將課程插入紅黑樹
                }
                // 設定全域課程列表
                Global.setCourseList(arrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener"); // 記錄錯誤
                // 處理異常
            }
        });
    }

    /**
     * 根據課程 ID 查詢課程。
     * 此方法會遍歷全域課程列表 Global.courseList 來尋找符合 ID 的課程。
     *
     * @param context 上下文物件 (目前未使用)。
     * @param id 要查詢的課程 ID。
     * @return 如果找到課程則回傳 Course 物件，否則回傳 null。
     */
    @Override
    public Course findCourseById(Context context, String id) {
        List<Course> courseList = Global.courseList; // 獲取全域課程列表
        // 遍歷課程列表
        for (Course course : courseList) {
            // 如果課程 ID 相符
            if (course.getId() == id) {
                return course; // 回傳找到的課程
            }
        }
        return null; // 未找到課程，回傳 null
    }

    /**
     * 獲取所有課程的註冊資訊。
     * 從 Firebase Firestore 的 "enroll" 集合中讀取資料，
     * 並將結果儲存到全域變數 Global.enrollInfo (一個以課程 ID 為鍵，註冊使用者 ID 列表為值的 Map)。
     */
    public void getCourseEnroll(){
        // 初始化全域註冊資訊 Map
        Global.enrollInfo=new HashMap<>();
        // 從 "enroll" 集合中獲取所有文件
        db.collection("enroll").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                // 遍歷查詢結果中的每個文件
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 獲取文件資料
                    Map<String, Object> data = documentSnapshot.getData();
                    String id=documentSnapshot.getId(); // 註冊記錄的 ID (目前未使用)
                    // 處理資料
                    String uid = data.get("uid").toString(); // 使用者 ID
                    String cid = data.get("cid").toString(); // 課程 ID
                    // 如果註冊資訊 Map 中不包含此課程 ID
                    if(!Global.enrollInfo.containsKey(cid)){
                        List<String>list=new ArrayList<>(); // 建立新的使用者 ID 列表
                        list.add(uid); // 將使用者 ID 加入列表
                        Global.enrollInfo.put(cid,list); // 將課程 ID 和使用者列表存入 Map
                    }else {
                        // 如果已存在此課程 ID，則將使用者 ID 加入現有的列表中
                        Global.enrollInfo.get(cid).add(uid);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener"); // 記錄錯誤
                // 處理異常
            }
        });
    }

    /**
     * 獲取目前使用者已註冊的課程。
     * 從 Firebase Firestore 的 "enroll" 集合中讀取資料，
     * 篩選出目前使用者註冊的課程 ID，並將對應的 Course 物件儲存到全域變數 Global.myCourseList。
     * 同時，將課程 ID 和註冊記錄 ID 的對應關係儲存到 Global.enrollCourse。
     */
    public void getUserCourse() {
        // 如果目前使用者為 null，則直接返回
        if(Global.currentUser==null)return;
        ArrayList<String> arrayList = new ArrayList<>(); // 用於儲存目前使用者註冊的課程 ID
        String userId = Global.currentUser.getId(); // 獲取目前使用者的 ID
        // 初始化全域已註冊課程 Map (課程 ID -> 註冊記錄 ID)
        Global.enrollCourse=new HashMap<>();
        // 從 "enroll" 集合中獲取所有文件
        db.collection("enroll").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                // 遍歷查詢結果中的每個文件
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 獲取文件資料
                    Map<String, Object> data = documentSnapshot.getData();
                    String id=documentSnapshot.getId(); // 註冊記錄的 ID
                    // 處理資料
                    String uid = data.get("uid").toString(); // 使用者 ID
                    String cid = data.get("cid").toString(); // 課程 ID
                    // 如果註冊記錄中的使用者 ID 與目前使用者 ID 相符
                    if (uid.equals(userId)) {
                        arrayList.add(cid); // 將課程 ID 加入列表
                        Global.enrollCourse.put(cid,id); // 將課程 ID 和註冊記錄 ID 存入 Map
                    }
                }
                // 設定目前使用者的課程列表
                setMyCourseList(arrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener"); // 記錄錯誤
                // 處理異常
            }
        });
    }

    /**
     * 根據課程 ID 列表設定目前使用者的課程列表 (Global.myCourseList)。
     * 此方法會遍歷傳入的課程 ID 列表，並從全域課程列表 (Global.courseList) 中找到對應的 Course 物件，
     * 然後將其加入到 Global.myCourseList (一個 HashSet，確保課程不重複)。
     *
     * @param courseList 包含課程 ID 的列表。
     */
    private void setMyCourseList(List<String>courseList){
        // 初始化全域使用者課程列表 (使用 HashSet 避免重複)
        Global.myCourseList=new HashSet<>();
        // 遍歷傳入的課程 ID 列表
        for(String cid:courseList){
            // 遍歷全域課程列表
            for(Course course:Global.courseList){
                // 如果課程 ID 相符
                if(course.getId().equals(cid)){
                    Global.myCourseList.add(course); // 將課程加入使用者的課程列表
                }
            }
        }
    }
}