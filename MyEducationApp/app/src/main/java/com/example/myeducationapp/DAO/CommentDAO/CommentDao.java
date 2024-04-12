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
 * @author u7532738 Jinhan Tan
 * CommentDao class
 */
public class CommentDao implements CommentDaoInterface {
    // get the FirebaseFirestore

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * find all comments stored in the firebase storage
     * store those comments in Global.comment list
     * store those comments in a map where
     * key is courseId and value is the arrayList store comments related to the course
     */
    public void findAllComments() {
        ArrayList<Comment> arrayList = new ArrayList<>();
        db.collection("commentList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                int i = 0;
                String courseid = null;
                Comment comment = null;
                //init tree and list
                Global.commentRBTree=new RBTree<>();
                Global.courseComment=new HashMap<>();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // get document data
                    Map<String, Object> data = documentSnapshot.getData();
                    // handle data
                    String commentid = documentSnapshot.getId();
                    courseid = data.get("courseId").toString();
                    String userid = data.get("userId").toString();
                    String content = data.get("content").toString();
                    String date = data.get("releaseDate").toString();
                    comment = new Comment(commentid, userid, courseid, content, date);
                    // insert the comment into tree
                    Global.commentRBTree.insert(comment);
                    // insert into map
                    linkCommentAndCourse(comment);
                }
                Global.commentList.clear();// = arrayList;
                Global.commentRBTree.toList(Global.commentList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener");
                // handle error
            }
        });
    }

    /**
     * get course id and put comment into the map enroll
     * @param comment comment
     */
    private void linkCommentAndCourse(Comment comment){
        if(comment==null)return;
        String cid=comment.getCourseId();
        if(!Global.courseComment.containsKey(cid)){
            RBTree<Comment> commentRBTree=new RBTree<>();
            commentRBTree.insert(comment);
            Global.courseComment.put(cid,commentRBTree);
        }else {
            Global.courseComment.get(cid).insert(comment);
        }
    }

    /**
     * when user post a new comment, update it to the firebase and update the device data
     * @param courseId
     * @param userId
     * @param content
     */
    public void postNewComment(String courseId,String userId,String content) {
        // get current time
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=format.format(calendar.getTime());
        HashMap<String, Object> CommentList = new HashMap<>();
        CommentList.put("courseId", courseId);
        CommentList.put("content", content);
        CommentList.put("userId", userId);
        CommentList.put("releaseDate",date );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //add to the firebase
        db.collection("commentList").document().set(CommentList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // update the device data
                        Global.update();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
