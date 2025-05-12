package com.example.myeducationapp.DAO.UserDAO;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.myeducationapp.Global;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference; // Firebase Realtime Database 參考 (目前未使用)
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
 * UserDao 類別，用於處理使用者資料的存取操作。
 * 實作了 UserDaoInterface 介面。
 */
public class UserDao implements UserDaoInterface {

    // 本地儲存使用者資料的檔案名稱
    final String filepath = "user.json";
    // Firebase Realtime Database 的參考 (目前在此類別中未使用)
    private DatabaseReference mDatabase;
    // Firebase Firestore 的實例，用於資料庫操作
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    /**
     * 將使用者資訊更新到本地 JSON 檔案。
     *
     * @param context 應用程式上下文。
     * @param user 要更新的使用者物件。如果為 null，則不執行任何操作。
     */
    @Override
    public void update(Context context, User user) {
        if(user==null)return; // 如果使用者物件為 null，則直接返回
        AssetManager assetManager = context.getAssets(); // 獲取 AssetManager
        InputStream inputStream;
        String fileName = "user.json"; // 要操作的檔案名稱
        try {
            // 這行程式碼嘗試從 assets 資料夾開啟檔案，但其結果 inputStream 並未被後續使用。
            // 主要操作的是 context.getFilesDir() 目錄下的檔案。
            inputStream = assetManager.open(fileName);
            BufferedReader br;
            File file = new File(context.getFilesDir(), filepath); // 獲取應用程式內部儲存空間的檔案
            Log.d("file path", file.getAbsolutePath()); // 記錄檔案的絕對路徑
            br = new BufferedReader(new FileReader(file)); // 建立 BufferedReader 以讀取檔案
            StringBuilder sb = new StringBuilder();
            String line;
            // 逐行讀取檔案內容
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString(); // 將讀取的內容轉換為字串
            Log.d("this is current string", jsonString); // 記錄目前的 JSON 字串
            JSONArray jsonArray = new JSONArray(jsonString); // 將字串轉換為 JSONArray
            JSONObject newUser = new JSONObject(); // 建立新的 JSONObject 來存放使用者資訊
            // 將使用者資訊放入 JSONObject
            newUser.put("id", user.getId());
            newUser.put("name", user.getName());
            newUser.put("email", user.getEmail());
            newUser.put("sex", user.getSex());
            newUser.put("image", user.getImgURL());
            newUser.put("password", user.getPwd());
            jsonArray.put(newUser); // 將新的使用者 JSONObject 加入 JSONArray
            String newJsonString = jsonArray.toString(); // 將更新後的 JSONArray 轉換回字串
            Log.d("this is the new string", newJsonString); // 記錄新的 JSON 字串
            file = new File(context.getFilesDir(), filepath); // 重新指定檔案物件 (雖然路徑相同)
            FileWriter fileWriter = new FileWriter(file); // 建立 FileWriter 以寫入檔案
            fileWriter.write(newJsonString); // 將新的 JSON 字串寫入檔案
            fileWriter.flush(); // 清空緩衝區，確保資料寫入
            Log.d("WRite success", newJsonString.substring(newJsonString.length() - 100)); // 記錄寫入成功訊息 (顯示末尾100字元)
            // OutputStream outputStream = null; // 此行宣告了未使用的 outputStream
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並印出異常
        }
    }


    /**
     * 從 Firebase Firestore 獲取所有使用者資訊。
     * 成功獲取後，會將使用者列表儲存到全域變數 Global.userList。
     */
    @Override
    public void findAllUsers() {
        ArrayList<User> userList = new ArrayList<>(); // 初始化使用者列表
        // 從 "userList" 集合中獲取所有文件
        db.collection("userList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                // 遍歷查詢結果中的每個文件
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // 獲取文件資料
                    Map<String, Object> data = documentSnapshot.getData();
                    Random r = new Random();
                    // 隨機選取一個頭像圖片索引
                    int i = r.nextInt(Global.drawables.size()); // 注意：Global.drawables 可能與頭像無關，應確認是否為 Global.headAvatars
                    // 處理資料
                    String uid = data.get("uid").toString(); // 使用者 ID
                    String name = data.get("name").toString(); // 使用者名稱
                    String email = data.get("email").toString(); // 使用者電子郵件
                    // 從全域頭像列表中隨機選取一個圖片 URL
                    String image = ""+Global.headAvatars.get(i % Global.headAvatars.size());
                    int sex = Integer.parseInt(data.get("sex").toString()); // 性別
                    // 建立 User 物件 (密碼欄位為空字串)
                    User user = new User(uid, name, email, sex, "", image);
                    userList.add(user); // 將使用者加入列表
                }
                Global.setUserList(userList); // 設定全域使用者列表
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
     * 根據使用者 ID 查詢使用者。
     * 此方法會遍歷全域使用者列表 Global.userList 來尋找符合 ID 的使用者。
     *
     * @param id 要查詢的使用者 ID。
     * @return 如果找到使用者則回傳 User 物件，否則回傳 null。
     */
    @Override
    public User findUserById(String id) {
        // 遍歷全域使用者列表
        for (User user : Global.userList) {
            // 如果使用者 ID 相符
            if(user.getId().equals(id)){
                return user; // 回傳找到的使用者
            }
        }
        return null; // 未找到使用者，回傳 null
    }

    /**
     * 從本地 JSON 檔案獲取所有使用者的電子郵件地址。
     *
     * @param context 應用程式上下文。
     * @return 包含所有使用者電子郵件的列表。如果發生錯誤則回傳空列表。
     */
    @Override
    public List<String> getAllUserEmails(Context context) {
        AssetManager assetManager = context.getAssets(); // 獲取 AssetManager
        ArrayList<String> arrayList = new ArrayList<>(); // 初始化電子郵件列表
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
                String email = jsonObject.getString("email"); // 獲取 "email" 欄位的值
                arrayList.add(email); // 將電子郵件加入列表
            }
        } catch (Exception e) {
            e.printStackTrace(); // 捕獲並印出異常
        }
        return arrayList; // 回傳電子郵件列表
    }

    /**
     * 更新使用者資訊到 Firebase Firestore。
     * 同時更新全域目前使用者 Global.currentUser。
     *
     * @param currentUser 要更新的使用者物件 (MyUser 型別)。
     */
    public void updateUserInfo(MyUser currentUser){
        Global.currentUser=currentUser; // 更新全域目前使用者
        String uid=currentUser.getId(); // 使用者 ID
        String email=currentUser.getEmail(); // 電子郵件
        String name=currentUser.getName(); // 名稱
        String sex=currentUser.sex+""; // 性別 (轉換為字串)
        HashMap<String, Object> userList = new HashMap<>(); // 建立儲存使用者資料的 Map
        // 將使用者資訊放入 Map
        userList.put("uid", uid);
        userList.put("email", email);
        userList.put("name", name);
        userList.put("sex", sex);
        userList.put("image", ""); // 圖片 URL 設為空字串 (可能需要根據實際需求調整)
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 獲取 Firestore 實例
        // 將使用者資料設定到 "userList" 集合中對應 uid 的文件
        db.collection("userList").document(uid).set(userList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // 更新成功時的操作 (目前為空)
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 更新失敗時的操作 (目前為空)
                        Log.e("UserDao", "Error updating user info to Firestore", e); // 建議記錄錯誤
                    }
                });
    }
}