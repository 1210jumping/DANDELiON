package com.example.myeducationapp.ui.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.ArrayList;

/**
 * ContactListActivity 類別。
 * 此 Activity 用於顯示使用者的聯絡人列表。
 * 使用者只能向與他們修讀相同課程的聯絡人傳送訊息。
 */
public class ContactListActivity extends AppCompatActivity {
    // 聯絡人列表的 Adapter
    ContactListAdapter adapter;
    // 儲存訊息提示的 ArrayList
    ArrayList<String> messageArrayList=new ArrayList<>();
    // 儲存聯絡人使用者的 ArrayList
    ArrayList<User>contactList=new ArrayList<>();
    // 顯示聯絡人列表的 RecyclerView
    RecyclerView contactRecyclerListView;

    /**
     * 在 Activity 建立時呼叫。
     * 初始化佈局、設定標題、啟用 ActionBar 的返回按鈕，並初始化聯絡人列表。
     *
     * @param savedInstanceState 如果 Activity 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設定 Activity 的佈局
        setContentView(R.layout.activity_contact_list);

        // 設定 Activity 標題
        setTitle("Contact List");
        // 獲取 ActionBar
        ActionBar actionBar = getSupportActionBar();
        // 獲取 RecyclerView 的參照
        contactRecyclerListView=findViewById(R.id.contactListRecyclerView);
        // 如果 ActionBar 存在，則啟用返回按鈕
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 初始化聯絡人列表視圖
        init();
    }

    /**
     * 初始化聯絡人列表視圖。
     * 此方法會從全域使用者列表和課程註冊資訊中篩選出聯絡人，
     * 並設定 RecyclerView 的 Adapter 和 LayoutManager。
     */
    private void init(){
        // 獲取聯絡人列表
        // 遍歷全域使用者列表
        for(User u: Global.userList){
            // 跳過目前使用者本人
            if(u.getId().equals(Global.currentUser.getId()))continue;
            // 遍歷目前使用者已選的課程列表
            for(Course course:Global.myCourseList){
                // 檢查該課程的註冊資訊是否存在，並且該使用者是否註冊了該課程
                if(Global.enrollInfo.get(course.getId())!=null&&Global.enrollInfo.get(course.getId()).contains(u.getId())){
                    // 如果使用者註冊了相同課程，則將其加入聯絡人列表
                    contactList.add(u);
                    // 為每個聯絡人新增預設訊息提示
                    messageArrayList.add("點擊以傳送訊息");
                    break; // 找到一個共同課程即可，無需繼續檢查其他課程
                }
            }
        }
        // 設定 RecyclerView 的 Adapter
        adapter=new ContactListAdapter(messageArrayList,contactList,getApplicationContext());
        contactRecyclerListView.setAdapter(adapter);
        // 設定 RecyclerView 的 LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contactRecyclerListView.setLayoutManager(layoutManager);
        // 為 RecyclerView 的項目之間新增分隔線
        contactRecyclerListView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                LinearLayoutManager.VERTICAL));
    }

    /**
     * 處理 ActionBar 選單項目點擊事件。
     * 如果點擊的是返回按鈕 (android.R.id.home)，則結束目前 Activity。
     *
     * @param item 被點擊的選單項目。
     * @return 如果事件已處理則回傳 true，否則回傳父類別的處理結果。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 檢查被點擊的選單項目 ID 是否為返回按鈕
        if (item.getItemId() == android.R.id.home) {
            finish(); // 結束目前 Activity
            return true; // 表示事件已處理
        }
        // 將事件交由父類別處理
        return super.onOptionsItemSelected(item);
    }
}