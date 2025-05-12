package com.example.myeducationapp.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.databinding.ActivityRegisterBinding;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * RegisterActivity 類別處理使用者註冊流程。
 * 它包含用於輸入使用者名稱、電子郵件和密碼的欄位，
 * 以及用於同意服務條款和隱私政策的核取方塊。
 * 成功註冊後，使用者資料將儲存到 Firebase Firestore。
 */
public class RegisterActivity extends AppCompatActivity {
    // 用於日誌記錄的標籤
    private static final String tag = "RegisterActivity";
    // 使用者唯一識別碼
    private String uid;
    // 使用者電子郵件
    private String email;
    // View binding 物件，用於存取佈局檔案中的視圖
    private ActivityRegisterBinding RegisterBinding;
    // Firebase 身份驗證實例
    private FirebaseAuth mAuth;

    /**
     * 當 Activity 第一次建立時呼叫。
     * 初始化視圖、Firebase 身份驗證、設定點擊監聽器和文字變更監聽器。
     *
     * @param savedInstanceState 如果 Activity 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 Firebase 身份驗證實例
        mAuth = FirebaseAuth.getInstance();
        // 更新全域變數（如果需要）
        Global.update();
        // 設定內容視圖為 activity_register.xml
        setContentView(R.layout.activity_register);
        // 初始化 View binding
        RegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        // 設定內容視圖為 binding 的根視圖
        setContentView(RegisterBinding.getRoot());

        // 在 ActionBar 中顯示返回按鈕
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 服務條款核取方塊設定
        CheckBox checkBox = RegisterBinding.TermsCheck;
        String text = checkBox.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        // 找到 "Terms of Use" 文字的起始和結束索引
        int startIndex = text.indexOf("Terms of Use");
        int endIndex = startIndex + "Terms of Use".length();
        // 建立可點擊的 Span
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 點擊時開啟 TermOfUseActivity
                Intent intent = new Intent(RegisterActivity.this, TermOfUseActivity.class);
                startActivity(intent);
                //finish();
            }
        };
        // 將可點擊的 Span 應用於文字
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(spannableString);
        // 使連結可點擊
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());


        // 隱私政策核取方塊設定
        CheckBox checkBoxPP = RegisterBinding.PrivacyCheck;
        String textPP = checkBoxPP.getText().toString();
        SpannableString spannableStringPP = new SpannableString(textPP);
        // 找到 "Privacy Policy" 文字的起始和結束索引
        int startIndexPP = textPP.indexOf("Privacy Policy");
        int endIndexPP = startIndexPP + "Privacy Policy".length();
        // 建立可點擊的 Span
        ClickableSpan clickableSpanPP = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 點擊時開啟 PrivacyPolicyActivity
                Intent intent = new Intent(RegisterActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                //finish();
            }
        };
        // 將可點擊的 Span 應用於文字
        spannableStringPP.setSpan(clickableSpanPP, startIndexPP, endIndexPP, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBoxPP.setText(spannableStringPP);
        // 使連結可點擊
        checkBoxPP.setMovementMethod(LinkMovementMethod.getInstance());


        // 初始化 UI 元件
        final Button JoinNowButton = RegisterBinding.joinnow; // 註冊按鈕
        final CheckBox YearCheck = RegisterBinding.TermsCheck; // 服務條款核取方塊
        final CheckBox PrivacyCheck = RegisterBinding.PrivacyCheck; // 隱私政策核取方塊
        final EditText user_name = RegisterBinding.name; // 使用者名稱輸入框
        final EditText email = RegisterBinding.email; // 電子郵件輸入框
        final EditText email_con = RegisterBinding.email2; // 確認電子郵件輸入框
        final EditText pwd = RegisterBinding.password2; // 密碼輸入框
        final EditText pwd_con = RegisterBinding.password3; // 確認密碼輸入框
        final ProgressBar loadingProgressBar = RegisterBinding.loading; // 載入進度條

        // 初始時禁用註冊按鈕
        JoinNowButton.setEnabled(false);

        // 註冊按鈕點擊監聽器
        JoinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 顯示載入進度條
                loadingProgressBar.setVisibility(View.VISIBLE);

                // 獲取輸入框中的文字
                String u_name = user_name.getText().toString();
                String u_email = email.getText().toString();
                String u_email_con = email_con.getText().toString();
                String u_pwd = pwd.getText().toString();
                String u_pwd_con = pwd_con.getText().toString();

                // 獲取現有使用者的電子郵件列表
                ArrayList<String> arrayList = new ArrayList<>();
                for(User u:Global.userList)
                    arrayList.add(u.getEmail());

                // 檢查使用者名稱是否為空
                if (u_name == null || u_name.length() == 0) {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.U_empty) // 提示使用者名稱為空
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else if (u_email == null || u_email.length() == 0 || u_email_con == null || u_email_con.length() == 0) {
                    // 檢查電子郵件是否為空
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.n_email) // 提示電子郵件為空
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else if (!u_email.equals(u_email_con)) {
                    // 檢查兩次輸入的電子郵件是否一致
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.c_email) // 提示電子郵件不一致
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else if (u_pwd == null || u_pwd.length() <= 5 || u_pwd_con == null || u_pwd_con.length() <= 5) {
                    // 檢查密碼長度是否小於等於 5
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.n_pwd) // 提示密碼長度不足
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else if (!u_pwd.equals(u_pwd_con)) {
                    // 檢查兩次輸入的密碼是否一致
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.c_pwd) // 提示密碼不一致
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else if (arrayList.contains(u_email)) {
                    // 檢查電子郵件是否已被註冊
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.xx_email) // 提示電子郵件已被註冊
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE); // 隱藏進度條
                                }
                            }).show();
                } else {
                    // 輸入正確，建立使用者
                    createUser(u_email, u_pwd, u_name);
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.notice)
                            .setMessage(R.string.Register_successful) // 提示註冊成功
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 跳轉到 NavigationActivity
                                    Intent intent = new Intent(RegisterActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                    finish(); // 結束目前 Activity
                                }
                            }).show();
                }
            }
        });

        // 服務條款核取方塊點擊監聽器
        YearCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 如果兩個核取方塊都被選中，則啟用註冊按鈕，否則禁用
                if (YearCheck.isChecked() && PrivacyCheck.isChecked()) {
                    JoinNowButton.setEnabled(true);
                } else {
                    JoinNowButton.setEnabled(false);
                }
            }
        });

        // 隱私政策核取方塊點擊監聽器
        PrivacyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 如果兩個核取方塊都被選中，則啟用註冊按鈕，否則禁用
                if (YearCheck.isChecked() && PrivacyCheck.isChecked()) {
                    JoinNowButton.setEnabled(true);
                } else {
                    JoinNowButton.setEnabled(false);
                }
            }
        });

        // 檢查輸入驗證
        TextWatcher afterTextChangedListener0 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
                // 在文字變更前檢查使用者名稱是否為空，如果是則設定錯誤訊息
                if (user_name == null || user_name.getText().toString().length() == 0)
                    user_name.setError("Not a valid username");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 在文字變更後檢查使用者名稱是否為空，如果是則設定錯誤訊息
                if (user_name == null || user_name.getText().toString().length() == 0)
                    user_name.setError("Not a valid username");
            }
        };
        user_name.addTextChangedListener(afterTextChangedListener0); // 為使用者名稱輸入框加入文字變更監聽器

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 驗證電子郵件輸入框的格式
                if (!isUserNameValid(email.getText().toString()))
                    email.setError("Not a valid email");
                // 驗證確認電子郵件輸入框的格式
                if (!isUserNameValid(email_con.getText().toString()))
                    email_con.setError("Not a valid email");

            }
        };
        email.addTextChangedListener(afterTextChangedListener); // 為電子郵件輸入框加入文字變更監聽器
        email_con.addTextChangedListener(afterTextChangedListener); // 為確認電子郵件輸入框加入文字變更監聽器

        TextWatcher afterTextChangedListener2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 驗證密碼長度是否大於 5
                if (pwd.getText().toString().length() < 5)
                    pwd.setError("Password length should be greater than 5");
                if (pwd_con.getText().toString().length() < 5)
                    pwd_con.setError("Password length should be greater than 5");

            }
        };
        pwd.addTextChangedListener(afterTextChangedListener2); // 為密碼輸入框加入文字變更監聽器
        pwd_con.addTextChangedListener(afterTextChangedListener2); // 為確認密碼輸入框加入文字變更監聽器
    }

    /**
     * 檢查使用者名稱是否有效。
     * 如果使用者名稱包含 "@" 符號，則驗證其是否為有效的電子郵件格式。
     * 否則，檢查使用者名稱是否非空（去除頭尾空白後）。
     * @author u6706220 Zhuxuan Yan
     * @param username 要驗證的使用者名稱。
     * @return 如果使用者名稱有效則回傳 true，否則回傳 false。
     */
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            // 如果包含 "@"，則使用 Android 的 Patterns.EMAIL_ADDRESS 進行驗證
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            // 否則，檢查是否非空字串（去除頭尾空白）
            return !username.trim().isEmpty();
        }
    }

    /**
     * 當 Activity 啟動或重新啟動時呼叫。
     * 檢查目前是否有 Firebase 使用者登入，並在日誌中記錄使用者資訊。
     */
    @Override
    public void onStart() {
        super.onStart();
        // 獲取目前的 Firebase 使用者
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // 如果使用者已登入，則在日誌中輸出使用者資訊
            Log.d("currentUser", currentUser.toString());
        }
    }

    // 用於標記 createUser 方法是否成功執行
    static boolean flag = false;
    /**
     * 使用電子郵件和密碼建立新使用者帳戶，並將使用者資訊儲存到 Firebase Firestore。
     *
     * @param mail 使用者的電子郵件。
     * @param pwd 使用者的密碼。
     * @param name 使用者的名稱。
     * @return 如果使用者帳戶建立成功且資料成功寫入 Firestore，則回傳 true；否則回傳 false。
     *         注意：此處的 flag 回傳可能存在異步問題，建議使用回呼或 Task API 處理結果。
     */
    private boolean createUser(String mail, String pwd, String name) {
        mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // 帳號建立成功
                    FirebaseUser fuser = mAuth.getCurrentUser();
                    uid = fuser.getUid(); // 獲取使用者唯一 ID
                    email = fuser.getEmail(); // 獲取使用者電子郵件

                    // 準備要儲存到 Firestore 的使用者資料
                    HashMap<String, Object> userList = new HashMap<>();
                    userList.put("uid", uid);
                    userList.put("email", email);
                    userList.put("name", name);
                    userList.put("sex", 1); // 預設性別為 1 (可根據需求調整)
                    userList.put("image", ""); // 預設圖片為空字串

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // 將使用者資料寫入 Firestore 的 "userList" 集合中，文件 ID 為使用者 UID
                    db.collection("userList").document(uid).set(userList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // 資料寫入成功
                                    Toast.makeText(RegisterActivity.this, "create user with email complete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    flag = true; // 設定成功標記
                                    // 建立本地 User 物件並更新全域變數
                                    User user = new User(uid, name, email, 1, "", "");
                                    Global.currentUser = MyUser.getInstance(user);
                                    Global.firebaseUser=fuser;
                                    Global.update(); // 更新全域使用者列表
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // 資料寫入失敗
                                    flag = false; // 設定失敗標記
                                    Log.w(tag, "Error adding document", e);
                                }
                            });
                } else {
                    // 帳號建立失敗
                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return flag; // 回傳操作結果標記 (注意異步問題)
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
        return super.onOptionsItemSelected(item); // 將事件交由父類別處理
    }
}