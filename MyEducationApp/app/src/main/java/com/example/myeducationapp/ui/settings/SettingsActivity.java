package com.example.myeducationapp.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myeducationapp.DAO.UserDAO.UserDao;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SettingsActivity 類別，用於管理應用程式的設定。
 * 此 Activity 包含一個 PreferenceFragmentCompat 來顯示和處理各種設定選項。
 */
public class SettingsActivity extends AppCompatActivity {
    // 位置權限請求代碼
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    /**
     * 在 Activity 建立時呼叫。
     * 初始化佈局，並載入設定 Fragment。
     * 同時設定 ActionBar 以顯示返回按鈕。
     *
     * @param savedInstanceState 如果 Activity 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        // 如果 savedInstanceState 為 null，表示是第一次建立 Activity
        if (savedInstanceState == null) {
            // 開始一個 Fragment 交易，將 SettingsFragment 替換到 R.id.settings 容器中
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        // 獲取 ActionBar
        ActionBar actionBar = getSupportActionBar();
        // 如果 ActionBar 存在，則啟用返回按鈕
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * SettingsFragment 內部類別，繼承自 PreferenceFragmentCompat。
     * 用於顯示和管理設定選項。
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        public static SettingsActivity settingsActivity; // 對 SettingsActivity 的靜態參照 (目前未使用)
        /**
         * 在建立 PreferenceFragment 時呼叫。
         * 從 XML 資源檔案載入設定選項，並設定各個選項的監聽器和初始值。
         *
         * @param savedInstanceState 如果 Fragment 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
         * @param rootKey 如果 Fragment 是巢狀的，則為根 PreferenceScreen 的鍵；否則為 null。
         */
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // 設定名稱 EditTextPreference
            EditTextPreference nameEditTextPreference = findPreference("name");
            nameEditTextPreference.setDefaultValue(Global.currentUser.getName()); // 設定預設值
            nameEditTextPreference.setText(Global.currentUser.getName()); // 設定目前文字
            // 設定名稱變更監聽器
            nameEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString()); // 記錄新值
                    Global.currentUser.setName(newValue.toString()); // 更新全域使用者名稱
                    new UserDao().updateUserInfo(Global.currentUser); // 更新資料庫中的使用者資訊
                    return true; // 表示已處理變更
                }
            });

            // 設定性別 ListPreference
            ListPreference genderListPreference = findPreference("gender");
            // 根據全域使用者的性別設定初始選項和摘要
            if(Global.currentUser.getSex() == 0){
                genderListPreference.setValueIndex(1); // 女性
                genderListPreference.setSummary("Female");
            } else if (Global.currentUser.getSex() == 1) {
                genderListPreference.setValueIndex(0); // 男性
                genderListPreference.setSummary("Male");
            }else if (Global.currentUser.getSex() == 2){
                genderListPreference.setValueIndex(2); // 其他
                genderListPreference.setSummary("Other");
            }else {
                genderListPreference.setValueIndex(3); // 不願透露
                genderListPreference.setSummary("Prefer not to say");
            }
            // 設定性別變更監聽器
            genderListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString()); // 記錄新值
                    // 根據選擇的值更新全域使用者的性別
                    if(newValue.toString().equals("Female")){
                        Global.currentUser.setSex(0);
                    } else if (newValue.toString().equals("Male")) {
                        Global.currentUser.setSex(1);
                    } else if (newValue.toString().equals("Other")) {
                        Global.currentUser.setSex(2);
                    }else {
                        Global.currentUser.setSex(3);
                    }
                    new UserDao().updateUserInfo(Global.currentUser); // 更新資料庫中的使用者資訊
                    genderListPreference.setSummary(newValue.toString()); // 更新摘要顯示
                    return true; // 表示已處理變更
                }
            });

            // 設定電子郵件 EditTextPreference
            EditTextPreference emailEditTextPreference = findPreference("email");
            emailEditTextPreference.setEnabled(false); // 設定為不可編輯
            emailEditTextPreference.setText(Global.currentUser.getEmail()); // 設定目前電子郵件

            // 設定地區 ListPreference
            ListPreference regionListPreference = findPreference("region");
            List<Locale> locales = getAllLocale(); // 獲取所有地區列表
            CharSequence entries[] = new String[locales.size()];
            CharSequence entrisValues[] = new String[locales.size()];
            int i = 0;
            // 填充地區選項的顯示名稱和值
            for(Locale l : locales){
                entries[i] = l.getDisplayCountry();
                entrisValues[i] = l.getDisplayCountry();
                i++;
            }
            regionListPreference.setEntries(entries); // 設定選項顯示名稱
            regionListPreference.setEntryValues(entrisValues); // 設定選項值
            String country = Global.currentUser.getCountry(); // 獲取目前使用者的國家
            String countryValue = Global.currentUser.getCountry();
            // 設定地區摘要和初始值
            if(country == null){
                regionListPreference.setSummary("Current country: Cannot get current location");
            }
                else{
                regionListPreference.setSummary("Current country: " + country);
                regionListPreference.setValue(countryValue);
            }
            // 設定地區變更監聽器
            regionListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString()); // 記錄新值
                    Global.currentUser.setCountry(newValue.toString()); // 更新全域使用者的國家
                    regionListPreference.setValue(Global.currentUser.getCountry()); // 設定新值
                    regionListPreference.setSummary("Current country: " +Global.currentUser.getCountry()); // 更新摘要
                    return false; // 表示未完全處理變更 (通常用於需要進一步操作的情況)
                }
            });

            // 設定語言 ListPreference
            ListPreference languageListPreference = findPreference("language");
            // 獲取目前應用程式的語言設定
            String language = getContext().getResources().getConfiguration().getLocales().get(0).getDisplayLanguage();
            Log.d("TAG", language); // 記錄目前語言
            String displayLanguage = "";
            // 根據系統語言設定顯示語言
            switch (language) {
                case "English":
                    displayLanguage = "English";
                    break;
                case "Chinese":
                    displayLanguage = "简体中文";
                    break;
                case "Japanese":
                    displayLanguage = "日本語";
                    break;
                case "French":
                    displayLanguage = "Français";
                    break;
                default:
                    // 預設語言或未匹配到的情況
                    break;
            }
            languageListPreference.setSummary("Current language: " + displayLanguage); // 設定語言摘要
            languageListPreference.setValue(displayLanguage); // 設定語言初始值
            // 設定語言變更監聽器
            languageListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString()); // 記錄新選擇的語言

                    Configuration config = new Configuration(); // 建立新的設定物件
                    Resources resources = getResources(); // 獲取資源物件
                    DisplayMetrics dm = resources.getDisplayMetrics(); // 獲取顯示指標

                    // 根據選擇的語言設定 Locale
                    switch (newValue.toString()){
                        case "English":
                            config.setLocale(Locale.ENGLISH);
                            break;
                        case "简体中文":
                            config.setLocale(Locale.SIMPLIFIED_CHINESE);
                            break;
                        case "繁體中文":
                            config.setLocale(Locale.TRADITIONAL_CHINESE);
                            break;
                        case "日本語":
                            config.setLocale(Locale.JAPANESE);
                            break;
                        case "Français":
                            config.setLocale(Locale.FRENCH);
                            break;
                        default:
                            config.setLocale(Locale.ENGLISH); // 預設為英文
                            break;
                    }
                    resources.updateConfiguration(config, dm); // 更新應用程式設定
                    languageListPreference.setValue(newValue.toString()); // 設定新選擇的語言值
                    languageListPreference.setSummary("Current Language: " +newValue.toString()); // 更新語言摘要

                    // 重新啟動 NavigationActivity 以套用語言變更
                    Intent intent = new Intent(getContext(), NavigationActivity.class);
                    startActivity(intent);
                    return false; // 表示未完全處理變更 (因為 Activity 會重新啟動)
                }
            });

            // 處理「登出」選項的點擊事件
            Preference logoutPreference = findPreference("logout");
            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showLogoutConfirmationDialog(); // 顯示登出確認對話框
                    return true; // 表示已處理點擊事件
                }
            });
        }

        /**
         * 獲取所有國家/地區的 Locale 列表。
         * 用於填充地區選擇列表。
         *
         * @return 包含所有國家/地區 Locale 的列表。
         */
        public List<Locale> getAllLocale(){
            List<Locale> mAllLocale  = new ArrayList<>();
            // 遍歷 ISO 國家代碼，為每個代碼建立 Locale 物件
            for (String str : Locale.getISOCountries()){
                mAllLocale.add(new Locale("",str));
            }
            return mAllLocale;
        }

        /**
         * 顯示登出確認對話框。
         * 詢問使用者是否確定要登出。
         */
        private void showLogoutConfirmationDialog() {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.logout_title) // 設定對話框標題
                    .setMessage(R.string.sure_logout) // 設定對話框訊息
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 使用者點擊「是」，執行登出操作
                            Intent intent = new Intent(getContext(), LoginActivity.class); // 建立前往登入頁面的 Intent
                            startActivity(intent); // 啟動登入頁面
                            Global.currentUser.logoutUser(); // 執行全域使用者的登出方法
                            getActivity().finish(); // 結束目前 Activity
                        }
                    })
                    .setNegativeButton(android.R.string.no, null) // 設定「否」按鈕，點擊後不執行任何操作
                    .setIcon(android.R.drawable.ic_dialog_alert) // 設定對話框圖示
                    .show(); // 顯示對話框
        }
    }

    /**
     * 處理 ActionBar 選單項目點擊事件。
     * 主要用於處理返回按鈕的點擊，結束目前 Activity。
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