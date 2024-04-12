package com.example.myeducationapp.DAO.UserDAO;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.myeducationapp.Global;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author u7532738 Jinhan Tan
 * UserDao class
 */
public class UserDao implements UserDaoInterface {

    final String filepath = "user.json";
    private DatabaseReference mDatabase;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    /**
     * update to local file
     * @param context
     * @param user
     */
    @Override
    public void update(Context context, User user) {
        if(user==null)return;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream;
        String fileName = "user.json";
        try {
            inputStream = assetManager.open(fileName);
            BufferedReader br;
            File file = new File(context.getFilesDir(), filepath);
            Log.d("file path", file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();
            Log.d("this is current string", jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject newUser = new JSONObject();
            newUser.put("id", user.getId());
            newUser.put("name", user.getName());
            newUser.put("email", user.getEmail());
            newUser.put("sex", user.getSex());
            newUser.put("image", user.getImgURL());
            newUser.put("password", user.getPwd());
            jsonArray.put(newUser);
            String newJsonString = jsonArray.toString();
            Log.d("this is the new string", newJsonString);
            file = new File(context.getFilesDir(), filepath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(newJsonString);
            fileWriter.flush();
            Log.d("WRite success", newJsonString.substring(newJsonString.length() - 100));
            OutputStream outputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get all user information from firebase
     */
    @Override
    public void findAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        db.collection("userList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // get document data
                    Map<String, Object> data = documentSnapshot.getData();
                    Random r = new Random();
                    int i = r.nextInt(Global.drawables.size());
                    // handle data
                    String uid = data.get("uid").toString();
                    String name = data.get("name").toString();
                    String email = data.get("email").toString();
                    String image = ""+Global.headAvatars.get(i % Global.headAvatars.size());
                    int sex = Integer.parseInt(data.get("sex").toString());
                    User user = new User(uid, name, email, sex, "", image);
                    userList.add(user);
                }
                Global.setUserList(userList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", "addOnFailureListener");
                //handle error
            }
        });
    }

    /**
     * get user by id
     * @param id
     * @return
     */
    @Override
    public User findUserById(String id) {
        for (User user : Global.userList) {
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    /**
     * get all user emails from local file
     * @param context
     * @return
     */
    @Override
    public List<String> getAllUserEmails(Context context) {
        AssetManager assetManager = context.getAssets();
        ArrayList<String> arrayList = new ArrayList<>();
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
                String email = jsonObject.getString("email");
                // creates the new user object
                arrayList.add(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * update user information to firebase
     * @param currentUser
     */
    public void updateUserInfo(MyUser currentUser){
        Global.currentUser=currentUser;
        String uid=currentUser.getId();
        String email=currentUser.getEmail();
        String name=currentUser.getName();
        String sex=currentUser.sex+"";
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
    }
}
