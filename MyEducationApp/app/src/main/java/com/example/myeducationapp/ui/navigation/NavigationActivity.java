package com.example.myeducationapp.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.example.myeducationapp.databinding.ActivityNavigationBinding;
import com.example.myeducationapp.ui.chat.ContactListActivity;
import com.example.myeducationapp.ui.register.PrivacyPolicyActivity;
import com.example.myeducationapp.ui.settings.HelpActivity;
import com.example.myeducationapp.ui.settings.SettingsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 應用程式主選單 Activity。
 * 此 Activity 作為應用程式的主要導覽中心，包含一個抽屜式導覽選單和一個頂部應用程式列。
 */
public class NavigationActivity extends AppCompatActivity {

    // AppBar 組態，用於管理 ActionBar 與 NavController 的整合
    private AppBarConfiguration mAppBarConfiguration;
    // View binding 物件，用於存取佈局檔案中的視圖
    private ActivityNavigationBinding binding;

    /**
     * 在 Activity 建立時呼叫。
     * 初始化佈局、工具列、浮動操作按鈕、抽屜式導覽選單以及導覽元件。
     *
     * @param savedInstanceState 如果 Activity 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 View binding
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 設定 ActionBar
        setSupportActionBar(binding.appBarNavigation.toolbar);
        // 設定浮動操作按鈕的點擊事件監聽器
        binding.appBarNavigation.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 顯示一個 Snackbar 訊息 (可替換為實際操作)
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // 啟動聯絡人列表 Activity
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivity(intent);
            }
        });
        // 獲取抽屜式佈局和導覽視圖
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // 獲取導覽視圖頭部佈局，並設定使用者名稱和電子郵件
        View headerLayout = navigationView.getHeaderView(0);
        TextView textViewUsername = headerLayout.findViewById(R.id.textViewUsername);
        textViewUsername.setText(Global.currentUser.getName());
        TextView textViewUserEmail = headerLayout.findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText(Global.currentUser.getEmail());
        // 獲取導覽視圖底部按鈕佈局，並設定按鈕點擊事件
        LinearLayout buttonLayout = navigationView.findViewById(R.id.button_layout);
        ImageButton button1 = buttonLayout.findViewById(R.id.button1); // 隱私權政策按鈕
        ImageButton button2 = buttonLayout.findViewById(R.id.button2); // 設定按鈕
        ImageButton button3 = buttonLayout.findViewById(R.id.button3); // 協助按鈕

        // 設定隱私權政策按鈕的點擊事件監聽器
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        // 設定設定按鈕的點擊事件監聽器
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // 設定協助按鈕的點擊事件監聽器
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        // 設定 AppBar 組態，將每個選單 ID 視為頂層目的地。
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_deadline,R.id.nav_home, R.id.nav_courselist)
                .setOpenableLayout(drawer)
                .build();
        // 獲取 NavController 並設定 ActionBar 與 NavController 的整合
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // 設定 NavigationView 與 NavController 的整合
        NavigationUI.setupWithNavController(navigationView, navController);

        // 設定工具列選單項目點擊事件監聽器
        binding.appBarNavigation.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 檢查點擊的選單項目是否為設定選項
                if (item.getItemId() == R.id.action_settings) {
                    // 啟動搜尋 Activity
                    Intent intent = new Intent(NavigationActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish(); // 結束目前 Activity
                    return true; // 表示事件已處理
                }
                return false; // 表示事件未處理
            }
        });
    }

    /**
     * 初始化 ActionBar 的選項選單內容。
     *
     * @param menu 要填充的選項選單。
     * @return 如果選單已顯示則回傳 true；否則回傳 false。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 擴充選單；如果 ActionBar 存在，則將項目加入到 ActionBar。
        getMenuInflater().inflate(R.menu.navigation, menu);
        menu.clear(); // 清除預設選單項目
        // 動態新增搜尋選單項目
        MenuItem menuItem = menu.add(Menu.NONE, R.id.action_settings, Menu.NONE, R.string.action_settings);
        menuItem.setIcon(R.drawable.ic_launcher_search); // 設定圖示
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); // 設定為永遠顯示在 ActionBar
        // 設定搜尋選單項目的點擊事件監聽器
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // 啟動搜尋 Activity
                Intent intent = new Intent(NavigationActivity.this, SearchActivity.class);
                startActivity(intent);
                return true; // 表示事件已處理
            }
        });
        return true;
    }

    /**
     * 當使用者點擊 ActionBar 上的向上導覽按鈕時呼叫。
     * 處理導覽邏輯，允許返回到上一個 Fragment 或 Activity。
     *
     * @return 如果向上導覽事件已處理則回傳 true；否則回傳父類別的處理結果。
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        // 嘗試使用 NavController 進行向上導覽，如果失敗則呼叫父類別的 onSupportNavigateUp 方法
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}