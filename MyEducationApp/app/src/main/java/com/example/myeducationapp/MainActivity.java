package com.example.myeducationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.DAO.UserDAO.UserDao;
import com.example.myeducationapp.ui.login.LoginActivity;

/**
 * 應用程式的主要入口 Activity。
 * 此 Activity 在應用程式啟動時執行，負責初始化全局資料並導向登入畫面。
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 當 Activity 首次被建立時呼叫。
     * 此方法負責初始化 Activity 的使用者介面、執行全局資料更新，
     * 並啟動 {@link LoginActivity}，然後結束自身。
     * @param savedInstanceState 如果 Activity 先前被銷毀後重新初始化，
     *                           此 Bundle 包含先前儲存的狀態資料。否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 呼叫父類別的 onCreate 方法
        init(); // 執行初始化操作，更新全局資料
        setContentView(R.layout.activity_main); // 設定此 Activity 的佈局檔案
        // 建立一個 Intent 以啟動 LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent); // 啟動 LoginActivity
        finish(); // 結束 MainActivity，使其不再返回堆疊中
    }

    /**
     * 初始化方法。
     * 呼叫 {@link Global#update()} 方法來更新應用程式的全局資料。
     */
    void init(){
         Global.update(); // 更新全局應用程式資料
    }

}