package com.example.myeducationapp.DAO.CommentDAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.Tree.RBTree;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * CommentDao 類別，用於處理評論資料的存取操作。
 * 包含從 Firebase Firestore 讀取評論、新增評論等功能。
 */
public class CommentDao implements CommentDaoInterface {
    // 獲取 FirebaseFirestore 實例
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * 從 Firebase Firestore 中尋找所有評論。
     * 將這些評論儲存在全域變數 Global.commentList 中。
     * 同時，將評論儲存在一個 Map 中，其中鍵是 courseId，值是與該課程相關的評論列表 (以 RBTree 形式儲存)。
     */
    public void findAllComments() {
        // 建立一個 ArrayList 來暫存評論 (目前未使用，但保留結構)
        ArrayList<Comment> arrayList = new ArrayList<>();
        // 從 "commentList" 集合中獲取所有文件
        db.collection("commentList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                // 初始化或清空全域評論紅黑樹和課程評論對應的 Map
                Global.commentRBTree = new RBTree<>();
                Global.courseComment = new HashMap<>();
                // 遍歷查詢結果中的每個文件
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 獲取文件資料
                    Map<String, Object> data = documentSnapshot.getData();
                    // 處理資料，將其轉換為 Comment 物件
                    String commentid = documentSnapshot.getId();
                    String courseid = data.get("courseId").toString();
                    String userid = data.get("userId").toString();
                    String content = data.get("content").toString();
                    String date = data.get("releaseDate").toString();
                    Comment comment = new Comment(commentid, userid, courseid, content, date);
                    // 將評論插入全域評論紅黑樹
                    Global.commentRBTree.insert(comment);
                    // 將評論與其所屬課程連結起來
                    linkCommentAndCourse(comment);
                }
                // 清空全域評論列表
                Global.commentList.clear();
                // 將全域評論紅黑樹中的所有評論轉換為列表形式，並存入 Global.commentList
                Global.commentRBTree.toList(Global.commentList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener"); // 記錄錯誤訊息
                // 處理錯誤
            }
        });
    }

    /**
     * 將指定的評論與其所屬的課程 ID 連結起來。
     * 如果課程 ID 在 Global.courseComment 中不存在，則為該課程建立一個新的紅黑樹來儲存評論。
     * 否則，將評論插入到現有的課程評論紅黑樹中。
     *
     * @param comment 要連結的評論物件。
     */
    private void linkCommentAndCourse(Comment comment){
        // 如果評論為 null，則直接返回
        if(comment==null)return;
        // 獲取評論的課程 ID
        String cid=comment.getCourseId();
        // 檢查 Global.courseComment 中是否已包含該課程 ID
        if(!Global.courseComment.containsKey(cid)){
            // 如果不包含，則為該課程建立一個新的評論紅黑樹
            RBTree<Comment> commentRBTree=new RBTree<>();
            commentRBTree.insert(comment); // 插入目前評論
            Global.courseComment.put(cid,commentRBTree); // 將課程 ID 和對應的紅黑樹存入 Map
        }else {
            // 如果已包含，則將評論插入到該課程對應的紅黑樹中
            Global.courseComment.get(cid).insert(comment);
        }
    }

    /**
     * 當使用者發表新評論時，將其更新到 Firebase 並更新裝置上的資料。
     *
     * @param courseId 評論所屬的課程 ID。
     * @param userId 發表評論的使用者 ID。
     * @param content 評論的內容。
     */
    public void postNewComment(String courseId,String userId,String content) {
        // 獲取目前時間
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 設定日期時間格式
        String date=format.format(calendar.getTime()); // 將目前時間格式化為字串

        // 建立一個 HashMap 來儲存評論資料
        HashMap<String, Object> CommentList = new HashMap<>();
        CommentList.put("courseId", courseId);
        CommentList.put("content", content);
        CommentList.put("userId", userId);
        CommentList.put("releaseDate",date );

        // 獲取 FirebaseFirestore 實例
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // 將新評論新增到 Firebase 的 "commentList" 集合中
        db.collection("commentList").document().set(CommentList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // 成功新增後，更新裝置上的全域資料
                        Global.update();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 處理新增失敗的情況 (目前為空)
                    }
                });
    }
}