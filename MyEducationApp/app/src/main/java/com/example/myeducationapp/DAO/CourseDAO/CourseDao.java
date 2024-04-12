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
 * @author u7532738 Jinhan Tan
 * CourseDao class
 */
public class CourseDao implements CourseDaoInterface {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void findAllCourses() {
        ArrayList<Course> arrayList = new ArrayList<>();
        db.collection("courseList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                Global.courseRBTree=new RBTree<>();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 获取文档数据
                    Random r = new Random();
                    int i = r.nextInt(Global.drawables.size());
                    Map<String, Object> data = documentSnapshot.getData();
                    // 处理数据
                    String cid = documentSnapshot.getId();
                    String cname = data.get("cname").toString();
                    String lecturer = data.get("lecturer").toString();
                    String image = Global.courseAvatars.get(i % Global.courseAvatars.size()).toString();
                    String date = data.get("releaseDate").toString();
                    String subject = data.get("subject").toString();
                    Course course = new Course(cid, cname, image, subject, lecturer, date);
                    arrayList.add(course);
                    Global.courseRBTree.insert(course);
                }
                Global.setCourseList(arrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener");
                // 处理异常
            }
        });
    }

    @Override
    public Course findCourseById(Context context, String id) {
        List<Course> courseList = Global.courseList;
        for (Course course : courseList) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }

    public void getCourseEnroll(){
        Global.enrollInfo=new HashMap<>();
        db.collection("enroll").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 获取文档数据
                    Map<String, Object> data = documentSnapshot.getData();
                    String id=documentSnapshot.getId();
                    // 处理数据
                    String uid = data.get("uid").toString();
                    String cid = data.get("cid").toString();
                    if(!Global.enrollInfo.containsKey(cid)){
                        List<String>list=new ArrayList<>();
                        list.add(uid);
                        Global.enrollInfo.put(cid,list);
                    }else {
                        Global.enrollInfo.get(cid).add(uid);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener");
                // 处理异常
            }
        });
    }

    public void getUserCourse() {
        if(Global.currentUser==null)return;
        ArrayList<String> arrayList = new ArrayList<>();
        String userId = Global.currentUser.getId();
        Global.enrollCourse=new HashMap<>();
        db.collection("enroll").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 获取文档数据
                    Map<String, Object> data = documentSnapshot.getData();
                    String id=documentSnapshot.getId();
                    // 处理数据
                    String uid = data.get("uid").toString();
                    String cid = data.get("cid").toString();
                    if (uid.equals(userId)) {
                        arrayList.add(cid);
                        Global.enrollCourse.put(cid,id);
                    }
                }
                setMyCourseList(arrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener");
                // 处理异常
            }
        });
    }

    private void setMyCourseList(List<String>courseList){
        Global.myCourseList=new HashSet<>();
        for(String cid:courseList){
            for(Course course:Global.courseList){
                if(course.getId().equals(cid)){
                    Global.myCourseList.add(course);
                }
            }
        }
    }
}
