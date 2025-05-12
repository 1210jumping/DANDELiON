package com.example.myeducationapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.login.LoginActivity;

/**
 * TermOfUseActivity 類別，用於顯示應用程式的使用條款。
 */
public class TermOfUseActivity extends AppCompatActivity {

    // 記錄來源 Activity
    private Activity fromActivity;
    /**
     * 在 Activity 建立時呼叫。
     * 初始化佈局、設定返回按鈕，並顯示使用條款內容。
     *
     * @param savedInstanceState 如果 Activity 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);
        // 從 Intent 中獲取來源 Activity (目前未使用)
        Activity fromActivity = Global.getActivityFromIntent(getIntent());
        // 啟用 ActionBar 上的返回按鈕
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 獲取用於顯示使用條款的 TextView
        TextView privacyTextView = findViewById(R.id.textView2);
        // 設定使用條款文字，並使用 Html.fromHtml 以支援 HTML 格式
        privacyTextView.setText(Html.fromHtml(getString(R.string.Terms_of_use)));
        // 使 TextView 中的連結可以點擊
        privacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
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