package com.example.myeducationapp.ui.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.databinding.ActivityLoginBinding;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.ui.register.PrivacyPolicyActivity;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.register.RegisterActivity;
import com.example.myeducationapp.ui.register.TermOfUseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 登入畫面的 Activity。
 * 負責處理使用者登入邏輯，包括輸入驗證、與 Firebase Authentication 的互動，
 * 以及根據地理位置設定語言。
 */
public class LoginActivity extends AppCompatActivity {

    /** ViewModel 用於處理登入相關的業務邏輯和資料。 */
    private LoginViewModel loginViewModel;
    /** ViewBinding 物件，用於方便地存取佈局檔案中的視圖。 */
    private ActivityLoginBinding binding;
    /** Firebase Authentication 的實例，用於處理使用者身份驗證。 */
    private FirebaseAuth mAuth;
    /** Firebase Firestore 的實例，用於存取資料庫。 */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /** 請求位置權限的請求碼。 */
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    /** 用於顯示載入狀態的進度條。 */
    ProgressBar loadingProgressBar;

    /**
     * 當 Activity 首次被建立時呼叫。
     * 初始化使用者介面、ViewModel、Firebase 服務，並設定各種事件監聽器。
     * 同時，它會檢查位置權限，並根據使用者選擇嘗試設定應用程式語言。
     * @param savedInstanceState 如果 Activity 先前被銷毀後重新初始化，
     *                           此 Bundle 包含先前儲存的狀態資料。否則為 null。
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Location[] location = {null}; // 用於儲存獲取到的位置資訊
        final String[] country = {null}; // 用於儲存根據位置獲取到的國家名稱

        // 檢查位置權限
        if (checkLocationPermission()) {
            try {
                // 顯示對話框詢問使用者是否根據目前位置設定語言
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Notice")
                        .setMessage("Do you want to use current location as your language?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                location[0] = getLocation();
                                country[0] = getAddress(location[0]);
                                setLanguage(country[0]);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

            }
            catch (Exception exception){
                // 處理獲取位置或設定語言時可能發生的例外
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.alert) // 警告標題
                        .setMessage(R.string.location_error) // 錯誤訊息
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 關閉對話框
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        }).show(); // 顯示錯誤對話框
            }
        }

        setContentView(R.layout.activity_login);

        Log.d("GG", "Oncreate");
        // 設定 Activity 的內容視圖，使用 ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 LoginViewModel
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // 從 binding 中獲取各個 UI 元件
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button RegisterButton = binding.register;
        loadingProgressBar = binding.loading;
        final TextView Terms = binding.Terms;
        final TextView Privacy = binding.Privacy;

        Log.d("create", "Oncreate"); // 記錄 Activity 建立

        loginButton.setEnabled(false); // 初始時登入按鈕不可用

        // 觀察登入表單狀態的變化
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid()); // 根據表單是否有效來設定登入按鈕的可用性
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError())); // 設定使用者名稱輸入框的錯誤提示
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError())); // 設定密碼輸入框的錯誤提示
                }
            }
        });

        // 觀察登入結果的變化
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError()); // 顯示登入失敗的提示
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess()); // 使用者登入成功後更新 UI
                }
                setResult(Activity.RESULT_OK); // 設定 Activity 結果為成功

                // 登入成功後可以選擇結束此 Activity
                // finish();
            }
        });

        // 文字變更監聽器，用於即時驗證輸入
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在文字變更前檢查，如果任一輸入框為空，則登入按鈕不可用
                if(usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
                    loginButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字變更後，通知 ViewModel 資料已變更
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                // 如果使用者名稱和密碼都不為空，則啟用登入按鈕
                if (!usernameEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals(""))
                    loginButton.setEnabled(true);
                // 如果密碼長度小於等於5，則登入按鈕不可用 (此處邏輯可能與 ViewModel 中的驗證重複)
                if (passwordEditText.getText().length() <= 5)
                    passwordEditText.setError("Password must be >5 characters");
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener); // 為使用者名稱輸入框設定文字變更監聽器
        passwordEditText.addTextChangedListener(afterTextChangedListener); // 為密碼輸入框設定文字變更監聽器

        // 監聽密碼輸入框的編輯器動作事件
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 當使用者按下 "完成" 鍵時，執行登入操作
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString()); // 呼叫 ViewModel 的登入方法
                }
                return false; // 表示事件未被完全處理，系統可以繼續處理
            }
        });


        // 註冊按鈕的點擊事件監聽器
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動註冊 Activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // "服務條款" 文字的點擊事件監聽器
        Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動服務條款 Activity
                Intent intent = new Intent(LoginActivity.this, TermOfUseActivity.class);
                startActivity(intent);
            }
        });

        // "隱私政策" 文字的點擊事件監聽器
        Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動隱私政策 Activity
                Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });


        // 登入按鈕的點擊事件監聽器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE); // 顯示進度條
                String userName = usernameEditText.getText().toString(); // 獲取使用者名稱
                String pwd = passwordEditText.getText().toString(); // 獲取密碼
                loginWithEmailAndPwd(userName, pwd); // 執行使用電子郵件和密碼登入的邏輯
            }
        });
    }

    public void updateUiWithUser(LoggedInUserView model) {
    }

    /**
     * 顯示登入失敗的提示訊息。
     * @param errorString 包含錯誤訊息的資源 ID。
     */
    public void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show(); // 顯示錯誤提示
    }

    /**
     * 檢查應用程式是否具有存取精確位置的權限。
     * 如果沒有權限，則會向使用者請求權限。
     * @return 如果已有權限則回傳 true，否則回傳 false。
     */
    private boolean checkLocationPermission() {
        // 檢查 ACCESS_FINE_LOCATION 權限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 如果沒有權限，則向使用者請求權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            return false; // 目前沒有權限
        }
        return true; // 已有權限
    }

    /**
     * 處理權限請求的結果。
     * @param requestCode 請求碼，用於識別是哪個權限請求。
     * @param permissions 請求的權限陣列。
     * @param grantResults 授予的權限結果陣列。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // 檢查位置權限請求的結果
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // 權限被拒絕，可以提示使用者或採取其他措施
                    Toast.makeText(this, R.string.x_location, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * 獲取目前裝置的最後已知位置。
     * 需要有位置權限才能成功獲取。
     * @return {@link Location} 物件，如果無法獲取位置則回傳 null。
     */
    private Location getLocation() {
        // 獲取 LocationManager 系統服務
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 設定位置提供者為網路提供者
        String provider = LocationManager.NETWORK_PROVIDER;
        if (provider == null) {
            return null;
        }
        // 再次檢查權限，確保在呼叫 getLastKnownLocation 前有權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return locationManager.getLastKnownLocation(provider); // 回傳最後已知位置
    }

    /**
     * 根據給定的地理位置資訊，獲取對應的國家名稱。
     *
     * @param location 包含經緯度等資訊的 {@link Location} 物件。
     * @return {@link String} 代表國家名稱。如果無法獲取地址或國家名稱，可能回傳 null 或拋出例外。
     */
    private String getAddress(Location location) {
        // 初始化 Geocoder，用於將經緯度轉換為地址資訊
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<String> res = null;  // 用於儲存查詢到的地址資訊
        List<Address> locationList = null; // 用於儲存查詢到的地址列表
        try {
            // 根據經緯度獲取最多10個符合的地址資訊
            locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
        } catch (IOException e) {
            e.printStackTrace(); // 發生 IO 例外時，列印錯誤堆疊
        }
        // 檢查是否成功獲取地址列表
        String country = locationList.get(0).getCountryName().toString();
        return country; // 回傳國家名稱
    }

    /**
     * 根據給定的國家名稱，設定應用程式的顯示語言。
     *
     * @param country {@link String} 代表國家名稱。
     */
    private void setLanguage(String country) {
        Configuration config = new Configuration(); // 建立新的設定物件
        Resources resources = getResources(); // 獲取應用程式的資源物件
        DisplayMetrics dm = resources.getDisplayMetrics(); // 獲取螢幕的顯示指標

        // 根據國家名稱設定對應的地區語言
        if (country.equals("China")) {
            config.setLocale(Locale.SIMPLIFIED_CHINESE); // 設定為簡體中文
        } else if (country.equals("French")) {
            config.setLocale(Locale.FRENCH); // 設定為法文
        } else if (country.equals("Japan")) {
            config.setLocale(Locale.JAPANESE); // 設定為日文
        } else if (country.equals("Taiwan")) {
            config.setLocale(Locale.TRADITIONAL_CHINESE); // 設定為繁體中文
        } else {
            config.setLocale(Locale.ENGLISH); // 預設設定為英文
        }
        // 更新應用程式的設定，使語言變更生效
        resources.updateConfiguration(config, dm);
    }

    /**
     * 用於標記登入操作狀態的靜態布林值。
     */
    static boolean flag = false;

    /**
     * 使用電子郵件和密碼嘗試登入 Firebase。
     * 成功登入後，會從 Firestore 獲取使用者詳細資料，更新全域使用者資訊，並導航至主應用程式介面。
     *
     * @param email 使用者的電子郵件地址。
     * @param pwd 使用者的密碼。
     * @return {@code boolean} 回傳靜態變數 {@code flag} 的目前值。由於 Firebase 操作的異步性，
     *         此回傳值通常在登入操作完成前就已確定，因此不能可靠地指示登入是否成功。
     */
    public boolean loginWithEmailAndPwd(String email, String pwd) {
        mAuth = FirebaseAuth.getInstance(); // 獲取 Firebase Authentication 實例
        // 使用電子郵件和密碼進行登入
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登入成功
                            Log.d("Login!", "UID:" + email); // 記錄登入成功訊息及使用者 Email
                            FirebaseUser fUser = mAuth.getCurrentUser(); // 獲取目前 Firebase 使用者物件
                            String uid = fUser.getUid(); // 獲取使用者的唯一 ID

                            // 從 Firestore 的 "userList" 集合中根據 UID 獲取使用者文件
                            db.collection("userList").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    // 成功獲取使用者文件快照
                                    Map<String, Object> data = documentSnapshot.getData(); // 將文件快照轉換為 Map
                                    // 從 Map 中提取使用者資料
                                    String uid = data.get("uid").toString();
                                    String name = data.get("name").toString();
                                    String email = data.get("email").toString();
                                    String image = data.get("image").toString();
                                    int sex = Integer.parseInt(data.get("sex").toString());
                                    // 建立 User 物件
                                    User user = new User(uid, name, email, sex, "", image);
                                    // 更新全域目前使用者資訊 (使用 MyUser 單例模式)
                                    Global.currentUser = MyUser.getInstance(user);
                                    Global.firebaseUser = fUser; // 更新全域 Firebase 使用者物件
                                    Global.update(); // 更新全域應用程式資料

                                    // 登入成功後，導向至 NavigationActivity
                                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                    finish(); // 結束目前的 LoginActivity
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        } else {
                            // 顯示登入失敗的提示對話框
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(R.string.alert) // 設定對話框標題 (從資源檔讀取)
                                    .setMessage(R.string.login_fail) // 設定對話框訊息 (從資源檔讀取)
                                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() { // 設定正面按鈕
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                        }
                                    }).show(); // 顯示對話框
                        }
                    }
                });
        return flag; // 回傳 flag 的值
    }
}