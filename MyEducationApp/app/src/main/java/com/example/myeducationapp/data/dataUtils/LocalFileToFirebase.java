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
 * @author u7532738 Jinhan Tan
 * LocalFileToFirebase class
 * this is a helper util class that will only be used once,
 * the purpose is to translate data from local file to firebase
 */
public class LocalFileToFirebase {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * get all user from local files, prepare for data transfer
     * @param context
     * @return
     */
    public List<User> findAllUsers(Context context) {
        AssetManager assetManager = context.getAssets();
        ArrayList<User> arrayList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    assetManager.open("user.json"), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();
            System.out.println(jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                // retrieves all the information from the current json dictionary in order to create
                // a new Course object
                String id = jsonObject.getString("id");
                int sex = Integer.parseInt(jsonObject.getString("sex"));
                String email = jsonObject.getString("email");
                String imgURL = jsonObject.getString("image");
                String pwd = jsonObject.getString("password");
                // creates the new Course object
                User user = new User(id, name, email, sex, pwd, imgURL);
                arrayList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * this is the function that write the user data from local file to firebase
     * @param context
     */
    public void writeUserListToFirebase(Context context) {
        ArrayList<User> LocalUsers = (ArrayList<User>) findAllUsers(context);
        for (User user : LocalUsers) {
            mAuth = FirebaseAuth.getInstance();
            String i = user.getId();
            String mail = user.getEmail();
            String pwd = user.getPwd();
            String name = user.getName();
            String sex = user.getSex() + "";
            mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        String email = user.getEmail();
                        HashMap<String, Object> userList = new HashMap<>();
                        userList.put("uid", uid);
                        userList.put("email", email);
                        userList.put("name", name);
                        userList.put("sex", sex);
                        userList.put("image", "");
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("userList").document(uid).set(userList)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    } else {

                    }
                }
            });
            mAuth.signInWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Login!", "UID:" + mail);
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                String email = user.getEmail();
                                HashMap<String, Object> userList = new HashMap<>();
                                userList.put("uid", uid);
                                userList.put("email", email);
                                userList.put("name", name);
                                userList.put("sex", sex);
                                userList.put("image", "");
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("userList").document(uid).set(userList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Success!", "UID:" + i);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } else {
                            }
                        }
                    });
        }
    }


    /**
     * this is a helper function to get all courses from local file
     * @param context
     * @return
     */
    public List<Course> findAllCourses(Context context) {
        AssetManager assetManager = context.getAssets();
        ArrayList<Course> arrayList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    assetManager.open("course.json"), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();
            System.out.println(jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("cname");
                // retrieves all the information from the current json dictionary in order to create
                // a new Course object
                String id = jsonObject.getString("cid");
                String lecturer = jsonObject.getString("lecturer");
                String subject = jsonObject.getString("subject");
                String imgURL = jsonObject.getString("imgURL");
                String datetime = jsonObject.getString("releaseDate");
                // creates the new Course object
                Course course = new Course(id, name, imgURL, subject, lecturer, datetime);
                arrayList.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * this is a function to write course data to firebase
     * @param context
     */
    public void writeCourseListToFirebase(Context context) {
        ArrayList<Course> LocalCourses = (ArrayList<Course>) findAllCourses(context);
        for (Course course : LocalCourses) {
            mAuth = FirebaseAuth.getInstance();
            String i = course.getId();
            String cname = course.getCname();
            String lecturer = course.getLecturer();
            String subject = course.getSubject();
            String date=course.getReleaseDate();
            HashMap<String, Object> CourseList = new HashMap<>();
            CourseList.put("cname", cname);
            CourseList.put("lecturer", lecturer);
            CourseList.put("subject", subject);
            CourseList.put("image", "");
            CourseList.put("releaseDate", date);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("courseList").document().set(CourseList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }


    /**
     * this is a helper function to read comment data from local file
     * @param context
     * @return
     */
    public List<Comment> findAllComments(Context context){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Comment> commentList = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(context.getAssets().open("comment.xml"));
            NodeList nodeList = document.getElementsByTagName("comment");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String course = element.getElementsByTagName("course_id").item(0).getTextContent();
                    String user = element.getElementsByTagName("user_id").item(0).getTextContent();
                    String datetime = element.getElementsByTagName("datetime").item(0).getTextContent();
                    String content = element.getElementsByTagName("content").item(0).getTextContent();
                    Comment comment = new Comment(""+(i + 1),user,course,content,datetime);
                    commentList.add(comment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentList;
    }

    /**
     * write comments to firebase
     * @param context
     */
    public void writeCommentListToFirebase(Context context) {
        ArrayList<Comment> LocalComments = (ArrayList<Comment>) findAllComments(context);
        for (Comment comment:LocalComments) {
            mAuth = FirebaseAuth.getInstance();
            String content = comment.getContent();
            String date = comment.getReleaseDate();
            int userid=Integer.parseInt(comment.getUserId());
            int courseid=Integer.parseInt(comment.getCourseId());
            if(userid>= Global.userList.size())userid=Global.userList.size()-1;
            if(courseid>=Global.courseList.size())courseid=Global.courseList.size()-1;
            String courseId = Global.courseList.get(courseid).getId();
            String userId=Global.userList.get(userid).getId();
            HashMap<String, Object> CommentList = new HashMap<>();
            CommentList.put("courseId", courseId);
            CommentList.put("content", content);
            CommentList.put("userId", userId);
            CommentList.put("releaseDate",date );
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("commentList").document().set(CommentList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            System.out.println("SUCCESS!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }
}




