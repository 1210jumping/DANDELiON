package com.example.myeducationapp.data.dataUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationapp.DAO.CommentDAO.Comment;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * LocalFileToFirebase 類別。
 * 這是一個輔助工具類別，僅使用一次，
 * 目的是將本地檔案中的資料轉換並寫入 Firebase。
 */
public class LocalFileToFirebase {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Firebase 驗證實例

    /**
     * 從本地檔案獲取所有使用者資訊，為資料傳輸做準備。
     *
     * @param context 應用程式上下文。
     * @return 包含所有使用者物件的列表。
     */
    public List<User> findAllUsers(Context context) {
        AssetManager assetManager = context.getAssets(); // 獲取 AssetManager
        ArrayList<User> arrayList = new ArrayList<>(); // 初始化使用者列表
        try {
            // 從 assets 資料夾中的 "user.json" 檔案讀取資料
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    assetManager.open("user.json"), "utf-8")); // 使用 UTF-8 編碼讀取
            StringBuilder sb = new StringBuilder();
            String line;
            // 逐行讀取檔案內容
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString(); // 將讀取的內容轉換為字串
            System.out.println(jsonString); // 在控制台印出 JSON 字串 (通常用於調試)
            JSONArray jsonArray = new JSONArray(jsonString); // 將字串轉換為 JSONArray
            // 遍歷 JSONArray 中的每個 JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name"); // 獲取使用者名稱
                // 從目前的 JSON 物件中檢索所有資訊以建立新的 User 物件
                String id = jsonObject.getString("id"); // 獲取使用者 ID
                int sex = Integer.parseInt(jsonObject.getString("sex")); // 獲取性別並轉換為整數
                String email = jsonObject.getString("email"); // 獲取電子郵件
                String imgURL = jsonObject.getString("image"); // 獲取圖片 URL
                String pwd = jsonObject.getString("password"); // 獲取密碼
                // 建立新的 User 物件
                User user = new User(id, name, email, sex, pwd, imgURL);
                arrayList.add(user); // 將使用者物件加入列表
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並印出異常
        }
        return arrayList; // 回傳使用者列表
    }

    /**
     * 將本地檔案中的使用者資料寫入 Firebase。
     *
     * @param context 應用程式上下文。
     */
    public void writeUserListToFirebase(Context context) {
        ArrayList<User> LocalUsers = (ArrayList<User>) findAllUsers(context); // 獲取本地所有使用者
        // 遍歷本地使用者列表
        for (User user : LocalUsers) {
            mAuth = FirebaseAuth.getInstance(); // 重新獲取 Firebase 驗證實例 (可考慮移至迴圈外)
            String i = user.getId(); // 使用者 ID (本地檔案中的 ID)
            String mail = user.getEmail(); // 電子郵件
            String pwd = user.getPwd(); // 密碼
            String name = user.getName(); // 名稱
            String sex = user.getSex() + ""; // 性別 (轉換為字串)
            // 使用電子郵件和密碼建立 Firebase 使用者帳號
            mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser(); // 獲取目前 Firebase 使用者
                        String uid = firebaseUser.getUid(); // Firebase 使用者唯一 ID
                        String email = firebaseUser.getEmail(); // Firebase 使用者電子郵件
                        HashMap<String, Object> userListMap = new HashMap<>(); // 建立儲存使用者資料的 Map
                        // 將使用者資訊放入 Map
                        userListMap.put("uid", uid);
                        userListMap.put("email", email);
                        userListMap.put("name", name);
                        userListMap.put("sex", sex);
                        userListMap.put("image", ""); // 圖片 URL 設為空字串
                        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 Firestore 實例
                        // 將使用者資料設定到 "userList" 集合中對應 uid 的文件
                        db.collection("userList").document(uid).set(userListMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // 寫入成功時的操作 (目前為空)
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 寫入失敗時的操作 (目前為空)
                                    }
                                });
                    } else {
                        // 建立帳號失敗時的操作 (目前為空)
                    }
                }
            });
            // 使用電子郵件和密碼登入 Firebase (通常在建立帳號後不需要立即登入，除非有特定流程需求)
            mAuth.signInWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Login!", "UID:" + mail); // 記錄登入成功訊息
                                FirebaseUser firebaseUser = mAuth.getCurrentUser(); // 獲取目前 Firebase 使用者
                                String uid = firebaseUser.getUid(); // Firebase 使用者唯一 ID
                                String email = firebaseUser.getEmail(); // Firebase 使用者電子郵件
                                HashMap<String, Object> userListMap = new HashMap<>(); // 建立儲存使用者資料的 Map
                                // 將使用者資訊放入 Map
                                userListMap.put("uid", uid);
                                userListMap.put("email", email);
                                userListMap.put("name", name);
                                userListMap.put("sex", sex);
                                userListMap.put("image", ""); // 圖片 URL 設為空字串
                                FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 Firestore 實例
                                // 將使用者資料設定到 "userList" 集合中對應 uid 的文件
                                db.collection("userList").document(uid).set(userListMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Success!", "UID:" + i); // 記錄寫入成功訊息 (使用本地 ID)
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // 寫入失敗時的操作 (目前為空)
                                            }
                                        });
                            } else {
                                // 登入失敗時的操作 (目前為空)
                            }
                        }
                    });
        }
    }


    /**
     * 從本地檔案獲取所有課程資訊的輔助函式。
     *
     * @param context 應用程式上下文。
     * @return 包含所有課程物件的列表。
     */
    public List<Course> findAllCourses(Context context) {
        AssetManager assetManager = context.getAssets(); // 獲取 AssetManager
        ArrayList<Course> arrayList = new ArrayList<>(); // 初始化課程列表
        try {
            // 從 assets 資料夾中的 "course.json" 檔案讀取資料
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    assetManager.open("course.json"), "utf-8")); // 使用 UTF-8 編碼讀取
            StringBuilder sb = new StringBuilder();
            String line;
            // 逐行讀取檔案內容
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString(); // 將讀取的內容轉換為字串
            System.out.println(jsonString); // 在控制台印出 JSON 字串 (通常用於調試)
            JSONArray jsonArray = new JSONArray(jsonString); // 將字串轉換為 JSONArray
            Gson gson = new Gson(); // Gson 實例 (在此處未使用)
            // 遍歷 JSONArray 中的每個 JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("cname"); // 獲取課程名稱
                // 從目前的 JSON 物件中檢索所有資訊以建立新的 Course 物件
                String id = jsonObject.getString("cid"); // 獲取課程 ID
                String lecturer = jsonObject.getString("lecturer"); // 獲取講師名稱
                String subject = jsonObject.getString("subject"); // 獲取科目
                String imgURL = jsonObject.getString("imgURL"); // 獲取圖片 URL
                String datetime = jsonObject.getString("releaseDate"); // 獲取發布日期
                // 建立新的 Course 物件
                Course course = new Course(id, name, imgURL, subject, lecturer, datetime);
                arrayList.add(course); // 將課程物件加入列表
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並印出異常
        }
        return arrayList; // 回傳課程列表
    }

    /**
     * 將課程資料寫入 Firebase 的函式。
     *
     * @param context 應用程式上下文。
     */
    public void writeCourseListToFirebase(Context context) {
        ArrayList<Course> LocalCourses = (ArrayList<Course>) findAllCourses(context); // 獲取本地所有課程
        // 遍歷本地課程列表
        for (Course course : LocalCourses) {
            mAuth = FirebaseAuth.getInstance(); // 重新獲取 Firebase 驗證實例 (可考慮移至迴圈外)
            String i = course.getId(); // 課程 ID (本地檔案中的 ID)
            String cname = course.getCname(); // 課程名稱
            String lecturer = course.getLecturer(); // 講師
            String subject = course.getSubject(); // 科目
            String date = course.getReleaseDate(); // 發布日期
            HashMap<String, Object> CourseListMap = new HashMap<>(); // 建立儲存課程資料的 Map
            // 將課程資訊放入 Map
            CourseListMap.put("cname", cname);
            CourseListMap.put("lecturer", lecturer);
            CourseListMap.put("subject", subject);
            CourseListMap.put("image", ""); // 圖片 URL 設為空字串
            CourseListMap.put("releaseDate", date);
            FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 Firestore 實例
            // 將課程資料新增到 "courseList" 集合中 (使用自動產生的文件 ID)
            db.collection("courseList").document().set(CourseListMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // 寫入成功時的操作 (目前為空)
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 寫入失敗時的操作 (目前為空)
                        }
                    });
        }
    }


    /**
     * 從本地檔案讀取留言資料的輔助函式。
     *
     * @param context 應用程式上下文。
     * @return 包含所有留言物件的列表。
     */
    public List<Comment> findAllComments(Context context){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 建立 DocumentBuilderFactory
        ArrayList<Comment> commentList = new ArrayList<>(); // 初始化留言列表
        try {
            DocumentBuilder builder = factory.newDocumentBuilder(); // 建立 DocumentBuilder
            // 從 assets 資料夾中的 "comment.xml" 檔案解析 XML 文件
            Document document = builder.parse(context.getAssets().open("comment.xml"));
            NodeList nodeList = document.getElementsByTagName("comment"); // 獲取所有 "comment" 標籤的節點列表

            // 遍歷節點列表
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i); // 獲取單個節點
                if (node.getNodeType() == Node.ELEMENT_NODE) { // 檢查節點是否為元素節點
                    Element element = (Element) node; // 將節點轉換為 Element
                    // 獲取各個子元素的文字內容
                    String course = element.getElementsByTagName("course_id").item(0).getTextContent();
                    String user = element.getElementsByTagName("user_id").item(0).getTextContent();
                    String datetime = element.getElementsByTagName("datetime").item(0).getTextContent();
                    String content = element.getElementsByTagName("content").item(0).getTextContent();
                    // 建立 Comment 物件 (ID 從 1 開始遞增)
                    Comment comment = new Comment(""+(i + 1),user,course,content,datetime);
                    commentList.add(comment); // 將留言物件加入列表
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並印出異常
        }
        return commentList; // 回傳留言列表
    }

    /**
     * 將留言列表寫入 Firebase Firestore。
     * 此方法會從本地檔案讀取留言，然後將每筆留言轉換並儲存到 Firestore 的 "commentList" 集合中。
     *
     * @param context 應用程式上下文，用於存取本地檔案。
     */
    public void writeCommentListToFirebase(Context context) {
        // 從本地檔案取得所有留言
        ArrayList<Comment> LocalComments = (ArrayList<Comment>) findAllComments(context);
        // 遍歷從本地檔案讀取的每一筆留言
        for (Comment comment:LocalComments) {
            // 取得 FirebaseAuth 實例 (注意：在迴圈中重複取得實例可能不是最高效的做法)
            mAuth = FirebaseAuth.getInstance();
            // 獲取留言內容
            String content = comment.getContent();
            // 獲取留言的發布日期
            String date = comment.getReleaseDate();
            // 將字串格式的使用者 ID 轉換為整數
            int userid=Integer.parseInt(comment.getUserId());
            // 將字串格式的課程 ID 轉換為整數
            int courseid=Integer.parseInt(comment.getCourseId());
            // 檢查轉換後的使用者 ID 是否超出全域使用者列表的索引範圍
            // 若超出範圍，則將 userid 設定為全域使用者列表的最後一個有效索引
            if(userid>= Global.userList.size())userid=Global.userList.size()-1;
            // 檢查轉換後的課程 ID 是否超出全域課程列表的索引範圍
            // 若超出範圍，則將 courseid 設定為全域課程列表的最後一個有效索引
            if(courseid>=Global.courseList.size())courseid=Global.courseList.size()-1;
            // 根據調整後的 courseid 從全域課程列表中取得實際的課程 ID
            String courseId = Global.courseList.get(courseid).getId();
            // 根據調整後的 userid 從全域使用者列表中取得實際的使用者 ID
            String userId=Global.userList.get(userid).getId();
            // 建立一個 HashMap 來儲存要上傳到 Firestore 的留言資料
            HashMap<String, Object> CommentList = new HashMap<>();
            // 將課程 ID 加入 HashMap
            CommentList.put("courseId", courseId);
            // 將留言內容加入 HashMap
            CommentList.put("content", content);
            // 將使用者 ID 加入 HashMap
            CommentList.put("userId", userId);
            // 將發布日期加入 HashMap
            CommentList.put("releaseDate",date );
            // 取得 FirebaseFirestore 的實例
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // 將 CommentList 中的資料設定到 "commentList" 集合下的一個新文件中 (文件 ID 自動產生)
            db.collection("commentList").document().set(CommentList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // 資料成功寫入 Firestore 時，在控制台印出 "SUCCESS!"
                            System.out.println("SUCCESS!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 資料寫入 Firestore 失敗時的處理邏輯 (目前為空)
                            // 建議在此處加入錯誤記錄或使用者提示
                            Log.e("LocalFileToFirebase", "寫入留言到 Firestore 時發生錯誤", e);
                        }
                    });
        }
    }
}




